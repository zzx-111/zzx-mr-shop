package com.baidu.shop.service.impl;

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
import com.baidu.shop.service.PageService;
import com.baidu.shop.utils.TenXunBeanUtil;
import com.github.pagehelper.PageInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 2 * @ClassName PageServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/8
 * 6 * @Version V1.0
 * 7
 **/
//@Service
public class PageServiceImpl  implements PageService {
   // @Autowired
    private BrandFeign brandFeign;

    //@Autowired
    private CategoryFeign categoryFeign;

    //@Autowired
    private GoodsFeign goodsFeign;

   // @Autowired
    private SpecificationFeign specificationFeign;

  //  @Override
    public Map<String, Object> getGoodsInfo(Integer spuId) {
        Map<String, Object> goodsInfoMap = new HashMap<>();
        //spu
        GoodsDTO spuDTO = new GoodsDTO();
        spuDTO.setId(spuId);
        Result<List<GoodsDTO>> goodsList = goodsFeign.getGoodsList(spuDTO);
        GoodsDTO spuResultData = null;
        if(goodsList.isStartsSuccess()){
            spuResultData = goodsList.getData().get(0);
            goodsInfoMap.put("spuInfo",spuResultData);
        }
        //spuDetail
        Result<SpecDetailDTO> spuDetailResult = goodsFeign.getSpecDetailBySpuId(spuId);
        if(spuDetailResult.isStartsSuccess()){
            goodsInfoMap.put("spuDetail",spuDetailResult.getData());
        }
        //分类信息
        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryListById(
                String.join(
                        ","
                        , Arrays.asList(spuResultData.getCid1() + "", spuResultData.getCid2() + ""
                                , spuResultData.getCid3() + "")
                )
        );
        if(categoryResult.isStartsSuccess()){
            goodsInfoMap.put("categoryInfo",categoryResult.getData());
        }
        //品牌信息
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(spuResultData.getBrandId());
        Result<PageInfo<BrandEntity>> brandResult = brandFeign.getBrandList(brandDTO);
        if(brandResult.isStartsSuccess()){
            goodsInfoMap.put("brandInfo",brandResult.getData().getList().get(0));
        }
        //sku
        Result<List<SpecSkuDTO>> skusResult = goodsFeign.getSkuBySpu(spuId);
        if(skusResult.isStartsSuccess()){
            goodsInfoMap.put("skus",skusResult.getData());
        }
        //规格组,规格参数(通用)
        SpecGroupDTO specGroupDTO = new SpecGroupDTO();
        specGroupDTO.setCid(spuResultData.getCid3());
        Result<List<SpecGroupEntity>> specGroupResult = specificationFeign.getSpecList(specGroupDTO);
        if(specGroupResult.isStartsSuccess()){

            List<SpecGroupEntity> specGroupList = specGroupResult.getData();
            List<SpecGroupDTO> specGroupAndParam = specGroupList.stream().map(specGroup -> {
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
            goodsInfoMap.put("specGroupAndParam",specGroupAndParam);
        }
        //特殊规格
        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(spuResultData.getCid3());
        specParamDTO.setGeneric(false);
        Result<List<SpectParamEntity>> specParamResult = specificationFeign.getSpecParamList(specParamDTO);
        if(specParamResult.isStartsSuccess()){
            List<SpectParamEntity> specParamEntityList = specParamResult.getData();
            Map<Integer, String> specParamMap = new HashMap<>();
            specParamEntityList.stream().forEach(specParam -> specParamMap.put(specParam.getId(),specParam.getName()));
            goodsInfoMap.put("specParamMap",specParamMap);
        }

        return goodsInfoMap;
    }
}
