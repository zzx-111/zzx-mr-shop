package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.dto.GoodsDTO;
import com.baidu.shop.dto.SpecDetailDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.dto.SpecSkuDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpectParamEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.response.GoodsDocResponse;
import com.baidu.shop.service.ShopElasticsearchService;
import com.baidu.shop.utils.EsUtils;
import com.baidu.shop.utils.JSONUtil;
import com.baidu.shop.utils.ObjectEqUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 2 * @ClassName ShopEsServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/4
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class ShopEsServiceImpl extends BaseApiService implements ShopElasticsearchService {

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplatel;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private BrandFeign brandFeign;



    @Override
    public GoodsDocResponse queryEs(String value, Integer page,String filter) {
        List<CategoryEntity> categoryEntities=null;
        Integer homCid=null;
        SearchHits<GoodsDoc> search = elasticsearchRestTemplatel.search(this.getQueryBuilder(value, page,filter).build(), GoodsDoc.class);
        List<SearchHit<GoodsDoc>> goodsList = EsUtils.getHighLightHit(search.getSearchHits());
        List<GoodsDoc> collect = goodsList.stream().map(goods -> {
            GoodsDoc content = goods.getContent();
            return content;
        }).collect(Collectors.toList());
        long total = search.getTotalHits();
        Double a = Math.ceil(Double.valueOf(total) / 10);
        long totalPage = a.longValue();
        Map<Integer, List<CategoryEntity>> categoryEntity = this.getCategoryEntity(search);
        for(Map.Entry<Integer, List<CategoryEntity>> entry:categoryEntity.entrySet()){
            categoryEntities=entry.getValue();
            homCid= entry.getKey();
        }


        return new GoodsDocResponse(total,totalPage,categoryEntities,this.getBrandEntity(search),collect,this.getSpecMap(homCid,value));
    }

    private NativeSearchQueryBuilder getQueryBuilder(String value,Integer page,String filter){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(filter) && filter.length()>2){
            Map<String, String> filterMap = JSONUtil.toMapValueString(filter);

            filterMap.forEach((k,v) -> {
                MatchQueryBuilder matchQueryBuilder=null;
                if(k.equals("brandId") || k.equals("cid3")){
                    matchQueryBuilder= QueryBuilders.matchQuery(k, v);
                }else{
                    matchQueryBuilder=QueryBuilders.matchQuery("specs."+k+".keyword", v);
                }
                boolQueryBuilder.must(matchQueryBuilder);
            });
            queryBuilder.withFilter(boolQueryBuilder);

        }
       // queryBuilder.withFilter(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(k, v)))

        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(value,"title"));
        queryBuilder.withHighlightBuilder(EsUtils.getHighlightSetField(value));
        queryBuilder.withPageable(PageRequest.of(page-1,10));
        //设置查询出来的内容,页面上做多只需要id,title,skus
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title","skus"}, null));
        //设置聚合.

        queryBuilder.addAggregation(AggregationBuilders.terms("agg_category").field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms("agg_brand").field("brandId"));
        return queryBuilder;
    }
    private Map<String, List<String>> getSpecMap(Integer hotCid,String search){
        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(hotCid);
        specParamDTO.setSearching(true);
        Result<List<SpectParamEntity>> specParamInfo = specificationFeign.getSpecParamList(specParamDTO);
        Map<String, List<String>> specMap = new HashMap<>();


        List<SpectParamEntity> specParamList = specParamInfo.getData();

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(
                    QueryBuilders.multiMatchQuery(search,"title","brandName","categoryName")
            );
            nativeSearchQueryBuilder.withPageable(PageRequest.of(0,1));
            specParamList.stream().forEach(specParam -> {
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName())
                        .field("specs." + specParam.getName() + ".keyword"));
            });

            SearchHits<GoodsDoc> searchHits = elasticsearchRestTemplatel.search(nativeSearchQueryBuilder.build(), GoodsDoc.class);
            Aggregations aggregations = searchHits.getAggregations();

            specParamList.stream().forEach(specParam -> {

                Terms aggregation = aggregations.get(specParam.getName());
                List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
                List<String> valueList = buckets.stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());

                specMap.put(specParam.getName(),valueList);
            });


        return specMap;
    }

    private Map<Integer, List<CategoryEntity>> getCategoryEntity(SearchHits<GoodsDoc>  search){
        Terms agg_category = search.getAggregations().get("agg_category");
        List<Integer> homCid = Arrays.asList(0);
        List<Integer> DocCount = Arrays.asList(0);
        Map<Integer, List<CategoryEntity>> map = new HashMap<>();
        List<String> categoryIdList = agg_category.getBuckets().stream().map(a ->{
            if(a.getDocCount() > DocCount.get(0)){
                DocCount.set(0,Integer.parseInt(a.getDocCount()+""));
                homCid.set(0, a.getKeyAsNumber().intValue());
            }
                return a.getKeyAsString();
        }).collect(Collectors.toList());


        Result<List<CategoryEntity>> categoryList=categoryFeign.getCategoryListById(String.join(",",categoryIdList));
        List<CategoryEntity> categoryEntities=null;
        if(categoryList.isStartsSuccess()){
            categoryEntities= categoryList.getData();
        }
        map.put(homCid.get(0), categoryEntities);


        return  map;
    }

    private List<BrandEntity> getBrandEntity(SearchHits<GoodsDoc>  search ){
        Terms agg_brand = search.getAggregations().get("agg_brand");
        List<String> brandIdList = agg_brand.getBuckets().stream().map(a -> a.getKeyAsString()).collect(Collectors.toList());
        Result<List<BrandEntity>> brandList=brandFeign.getBrandListById(String.join(",",brandIdList));
        List<BrandEntity> brandEntities=null;
        if(brandList.isStartsSuccess()){
            brandEntities = brandList.getData();
        }
        return brandEntities;
    }


    @Override
    public Result<JSONObject> initGoodsEsData() {
        IndexOperations indexOperations = elasticsearchRestTemplatel.indexOps(GoodsDoc.class);
        if(!indexOperations.exists()){
            indexOperations.create();
            indexOperations.createMapping();
        }
        List<GoodsDoc> goodsDocs = this.esGoodsInfo(new GoodsDTO());
        if(!goodsDocs.isEmpty()){
           goodsDocs.forEach( a ->{
               System.out.println(a);
           });
            elasticsearchRestTemplatel.save(goodsDocs);
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> saveData(Integer spuId) {
        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setId(spuId);
        List<GoodsDoc> goodsDocs = this.esGoodsInfo(goodsDTO);
        elasticsearchRestTemplatel.save(goodsDocs.get(0));
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> delData(Integer spuId) {
        GoodsDoc goodsDoc = new GoodsDoc();
        goodsDoc.setId(spuId.longValue());
        elasticsearchRestTemplatel.delete(goodsDoc);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> clearGoodsEsData() {
        IndexOperations indexOperations = elasticsearchRestTemplatel.indexOps(GoodsDoc.class);
        indexOperations.delete();
        return this.setResultSuccess();
    }



    private List<GoodsDoc> esGoodsInfo(GoodsDTO good ) {


//        good.setPage(1);
//        good.setRows(10);
        Result<List<GoodsDTO>> goodsList = goodsFeign.getGoodsList(good);
        //获取spu数据.并填充数据.
        if(goodsList.isStartsSuccess()){
            //获取spu列表
            List<GoodsDTO> spuList = goodsList.getData();
            List<GoodsDoc> collect = spuList.stream().map(goods -> {
                //填充spu数据
                GoodsDoc goodsDoc = new GoodsDoc();
                goodsDoc.setId(goods.getId().longValue());
                goodsDoc.setBrandId(goods.getBrandId().longValue());
                goodsDoc.setTitle(goods.getTitle());
                goodsDoc.setBrandName(goods.getBrandName());
                goodsDoc.setCategoryName(goods.getCategoryName());
                goodsDoc.setCid1(goods.getCid1().longValue());
                goodsDoc.setCid2(goods.getCid2().longValue());
                goodsDoc.setCid3(goods.getCid3().longValue());
                goodsDoc.setSubTitle(goods.getSubTitle());
                goodsDoc.setCreateTime(goods.getCreateTime());
                //通过spuId查询多条sku数据.
                Result<List<SpecSkuDTO>> skuBySpuList = goodsFeign.getSkuBySpu(goods.getId());
                //判断查询是否成功
                if (skuBySpuList.isStartsSuccess()) {
                    Map<List<Long>, List<Map<String, Object>>> priceAndSku = this.getPriceAndSku(skuBySpuList);
                    priceAndSku.forEach((price,sku) ->{
                        //将sku数据填入到GoodsDoc中
                        goodsDoc.setSkus(JSONUtil.toJsonString(sku));
                        //将当前spu下的所有sku的价格存入GoodsDoc中
                        goodsDoc.setPrice(price) ;
                    });
                    //创建规格参数DTO
                    SpecParamDTO specParamDTO = new SpecParamDTO();
                    //通过分类的最后一级id查询规格参数.
                    specParamDTO.setCid(goods.getCid3());
                    //只查询支持搜索的
                    specParamDTO.setSearching(true);
                    //通过分类id查询规格参数数据
                    Result<List<SpectParamEntity>> specParamList = specificationFeign.getSpecParamList(specParamDTO);
                    if (specParamList.isStartsSuccess()) {
                     //获取specParam数据
                        Map<String, Object> paramsMap = this.getParamsMap(goods.getId(), specParamList);
                        goodsDoc.setSpecs(paramsMap);
                    }

                }
                return goodsDoc;

            }).collect(Collectors.toList());
            return collect;

        }

        return null;
    }

    private  Map<List<Long>,List<Map<String, Object>>> getPriceAndSku(Result<List<SpecSkuDTO>> skuBySpuList){
        Map<List<Long>,List<Map<String, Object>>> map = new HashMap<>();
        //获取sku列
        List<SpecSkuDTO> skuList = skuBySpuList.getData();

        //创建price列表准备加入数据
        List<Long> priceList = new ArrayList<>();

        //当前spu下的所有sku数据
        List<Map<String, Object>> skuAllList = skuList.stream().map(sku -> {
            //获取sku里需要的数据并放到map中.
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("image", sku.getImages());
            skuMap.put("price", sku.getPrice());
            //价格加入到list
            priceList.add(sku.getPrice().longValue());
            return skuMap;

        }).collect(Collectors.toList());
        map.put(priceList,skuAllList);
        return map;
    }
    private Map<String,Object> getParamsMap(Integer goodsId,Result<List<SpectParamEntity>> specParamList){
        Result<SpecDetailDTO> specDetailBySpuId = goodsFeign.getSpecDetailBySpuId(goodsId);
        if (specDetailBySpuId.isStartsSuccess()) {
            SpecDetailDTO detailData = specDetailBySpuId.getData();
            //获取通用属性.
            Map<String, String> tongYo = JSONUtil.toMapValueString(detailData.getGenericSpec());
            //获取私有属性.
            Map<String, List<String>> siYo = JSONUtil.toMapValueStrList(detailData.getSpecialSpec());
            Map<String, Object> paramsMap = new HashMap<>();
            List<SpectParamEntity> specParam = specParamList.getData();
            specParam.stream().forEach(param -> {
              
                //判断是否是数值类型
                if (param.getGeneric()) {
                    //判断是否是数值类型.以及范围不为空
                    if (param.getNumeric() && ObjectEqUtil.isNotNull(param.getSegments())) {

                        paramsMap.put(param.getName(), chooseSegment(tongYo.get(param.getId() + ""), param.getSegments(), param.getUnit()));

                    } else {
                        paramsMap.put(param.getName(), tongYo.get(param.getId() + ""));

                    }
                } else {
                    paramsMap.put(param.getName(), siYo.get(param.getId() + ""));
                }



            });
            return paramsMap;

        }
        return null;
    }


    private String chooseSegment(String value, String segments, String unit) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : segments.split(",")) {
            String[] segs = segment.split("-");
        // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
        // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + unit + "以上";
                }else if(begin == 0){
                    result = segs[1] + unit + "以下";
                }else{
                    result = segment + unit;
                }
                break;
            }
        }
        return result;
    }


}
