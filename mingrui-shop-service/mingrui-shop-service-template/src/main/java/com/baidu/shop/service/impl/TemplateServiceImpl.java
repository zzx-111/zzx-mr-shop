package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpectParamEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.service.TemplateService;
import com.baidu.shop.utils.ObjectEqUtil;
import com.baidu.shop.utils.TenXunBeanUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 2 * @ClassName TemplateServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/9
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class TemplateServiceImpl extends BaseApiService implements TemplateService {
    @Value(value = "${mrshop.static.html.path}")
    private String pathHtml;

     @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private GoodsFeign goodsFeign;

     @Autowired
    private SpecificationFeign specificationFeign;

     @Autowired
     private TemplateEngine templateEngine;
    @Override
    public Result<JSONObject> createStaticHTMLTemplate(Integer spuId) {
        Map<String, Object> goodsInfo = this.getGoodsInfo(spuId);


        File file = new File(this.pathHtml,spuId+".html");
        PrintWriter printWriter=null;
        if(!file.exists()){
            try {
                Context context = new Context();
                context.setVariables(goodsInfo);
                file.createNewFile();
                printWriter = new PrintWriter(file, "UTF-8");
                templateEngine.process("item", context,printWriter);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(ObjectEqUtil.isNotNull(printWriter)){
                    printWriter.close();
                }
            }
        }

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> initStaticHTMLTemplate() {
        Result<List<GoodsDTO>> goodsList = goodsFeign.getGoodsList(new GoodsDTO());
      if(goodsList.isStartsSuccess()){
          goodsList.getData().stream().forEach(spu ->{
              this.createStaticHTMLTemplate(spu.getId());
          });
      }
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> clearStaticHTMLTemplate() {
        Result<List<GoodsDTO>> goodsList = goodsFeign.getGoodsList(new GoodsDTO());
        if(goodsList.isStartsSuccess()){
            goodsList.getData().stream().forEach(spu ->{
                this.deleteStaticHTMLTemplate(spu.getId());
            });
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> deleteStaticHTMLTemplate(Integer spuId) {
        File file = new File(this.pathHtml,spuId+".html");
        if(file.exists()){
            file.delete();
        }
        return this.setResultSuccess();
    }

    public Map<String, Object> getGoodsInfo(Integer spuId) {
        Map<String, Object> goodsInfoMap = new HashMap<>();
        GoodsDTO spuResultData = this.getSpuInfo(spuId);
        //spu
        goodsInfoMap.put("spuInfo",spuResultData);
        //spuDetail
        goodsInfoMap.put("spuDetail", goodsInfoMap.put("spuDetail",spuId));
        //分类信息
        goodsInfoMap.put("categoryInfo",spuResultData);

        //品牌信息
        goodsInfoMap.put("brandInfo",this.getBrandInfo(spuResultData.getBrandId()));
        //sku
        goodsInfoMap.put("skus",this.getSkus(spuId));
        //规格组,规格参数(通用)
        goodsInfoMap.put("specGroupAndParam",this.getSpecGroupAndParam(spuResultData.getCid3()));

        //特殊规格
        goodsInfoMap.put("specParamMap",this.getSpecParamMap(spuResultData.getCid3()));

        return goodsInfoMap;
    }

    private GoodsDTO getSpuInfo(Integer spuId){
        GoodsDTO spuDTO = new GoodsDTO();
        spuDTO.setId(spuId);
        Result<List<GoodsDTO>> goodsList = goodsFeign.getGoodsList(spuDTO);
        GoodsDTO spuResultData = null;
        if(goodsList.isStartsSuccess()){
            spuResultData = goodsList.getData().get(0);

        }
        return spuResultData;
    }

    private  SpecDetailDTO getSpuDetail(Integer spuId){
        SpecDetailDTO  data=null;
        Result<SpecDetailDTO> spuDetailResult = goodsFeign.getSpecDetailBySpuId(spuId);
        if(spuDetailResult.isStartsSuccess()){
             data = spuDetailResult.getData();

        }
        return data;
    }

    private List<CategoryEntity> getCategoryInfo( GoodsDTO spuResultData){
        List<CategoryEntity> data=null;
        //分类信息
        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryListById(
                String.join(
                        ","
                        , Arrays.asList(spuResultData.getCid1() + "", spuResultData.getCid2() + ""
                                , spuResultData.getCid3() + "")
                )
        );
        if(categoryResult.isStartsSuccess()){
           data = categoryResult.getData();
        }
        return data;
    }

    private BrandEntity getBrandInfo(Integer brandId){
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brandId);
        Result<PageInfo<BrandEntity>> brandResult = brandFeign.getBrandList(brandDTO);
        BrandEntity brandEntity=null;
        if(brandResult.isStartsSuccess()){
            brandEntity = brandResult.getData().getList().get(0);

        }
        return brandEntity;
    }

    private  List<SpecSkuDTO> getSkus(Integer spuId){
        Result<List<SpecSkuDTO>> skusResult = goodsFeign.getSkuBySpu(spuId);
        List<SpecSkuDTO> data=null;
        if(skusResult.isStartsSuccess()){
            data = skusResult.getData();
        }
        return data;
    }
    private  List<SpecGroupDTO> getSpecGroupAndParam(Integer cid3){
        List<SpecGroupDTO> specGroupAndParam=null;
        SpecGroupDTO specGroupDTO = new SpecGroupDTO();
        specGroupDTO.setCid(cid3);
        Result<List<SpecGroupEntity>> specGroupResult = specificationFeign.getSpecList(specGroupDTO);
        if(specGroupResult.isStartsSuccess()){

            List<SpecGroupEntity> specGroupList = specGroupResult.getData();
            specGroupAndParam = specGroupList.stream().map(specGroup -> {
                SpecGroupDTO specGroupDTO1 = TenXunBeanUtil.copyProperties(specGroup, SpecGroupDTO.class);

                SpecParamDTO specParamDTO = new SpecParamDTO();
                specParamDTO.setGroupId(specGroupDTO1.getId());
                specParamDTO.setGeneric(true);
                Result<List<SpectParamEntity>> specParamResult = specificationFeign.getSpecParamList(specParamDTO);
                if (specParamResult.isStartsSuccess()) {
                    specGroupDTO1.setSpecList(specParamResult.getData());
                }
                return specGroupDTO1;
            }).collect(Collectors.toList());

        }
        return specGroupAndParam;
    }

    private Map<Integer, String> getSpecParamMap(Integer cid3){
        Map<Integer, String> specParamMap = new HashMap<>();
        //特殊规格
        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(cid3);
        specParamDTO.setGeneric(false);
        Result<List<SpectParamEntity>> specParamResult = specificationFeign.getSpecParamList(specParamDTO);
        if(specParamResult.isStartsSuccess()){
            List<SpectParamEntity> specParamEntityList = specParamResult.getData();

            specParamEntityList.stream().forEach(specParam -> specParamMap.put(specParam.getId(),specParam.getName()));

        }
        return specParamMap;

    }
}
