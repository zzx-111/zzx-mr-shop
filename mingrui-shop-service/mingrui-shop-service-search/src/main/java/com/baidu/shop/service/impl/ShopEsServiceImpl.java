package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.dto.GoodsDTO;
import com.baidu.shop.dto.SpecDetailDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.dto.SpecSkuDTO;
import com.baidu.shop.entity.SpectParamEntity;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.service.ShopElasticsearchService;
import com.baidu.shop.utils.JSONUtil;
import com.baidu.shop.utils.ObjectEqUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Result<JSONObject> esGoodsInfo() {

        GoodsDTO good = new GoodsDTO();
        good.setPage(1);
        good.setRows(10);
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
                    //获取sku列表
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
                    //将sku数据填入到GoodsDoc中
                    goodsDoc.setSkus(JSONUtil.toJsonString(skuAllList));
                    //将当前spu下的所有sku的价格存入GoodsDoc中
                    goodsDoc.setPrice(priceList);

                    //创建规格参数DTO
                    SpecParamDTO specParamDTO = new SpecParamDTO();
                    //通过分类的最后一级id查询规格参数.
                    specParamDTO.setCid(goods.getCid3());
                    //只查询支持搜索的
                    specParamDTO.setSearching(true);
                    //通过分类id查询规格参数数据
                    Result<List<SpectParamEntity>> specParamList = specificationFeign.getSpecParamList(specParamDTO);
                    if (specParamList.isStartsSuccess()) {
                        //获取SpuDetail数据
                        Result<SpecDetailDTO> specDetailBySpuId = goodsFeign.getSpecDetailBySpuId(goods.getId());
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
                            goodsDoc.setSpecs(paramsMap);

                        }


                    }


                }
                return goodsDoc;

            }).collect(Collectors.toList());

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
