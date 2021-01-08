package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.GoodsDTO;
import com.baidu.shop.dto.SpecDetailDTO;
import com.baidu.shop.dto.SpecSkuDTO;
import com.baidu.shop.entity.GoodsEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 2 * @ClassName GoodsService
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/5
 * 6 * @Version V1.0
 * 7
 **/
@Api(tags = "商品管理接口")
public interface GoodsService {

    @ApiOperation(value = "获取商品列表")
    @GetMapping("goods/list")
    Result<List<GoodsEntity>> getGoodsList(GoodsDTO goodsDTO);

    @ApiOperation(value = "新增商品")
    @PostMapping("goods/save")
    Result<JsonObject>  saveGoods( @Validated( {MingruiOperation.add.class}) @RequestBody GoodsDTO goodsDTO);


    @ApiOperation(value = "通过spuId删除商品")
    @DeleteMapping("goods/delete")
    Result<JsonObject>  deleteGoods(Integer spuId);

    @ApiOperation(value = "修改商品")
    @PutMapping("goods/save")
    Result<JsonObject>  editGoods( @Validated( {MingruiOperation.update.class}) @RequestBody GoodsDTO goodsDTO);


    @ApiOperation(value = "修改商品上下架")
    @PutMapping("goods/xiaJia")
    Result<JsonObject>  xiaJiaGoods( @Validated( {MingruiOperation.update.class}) @RequestBody GoodsDTO goodsDTO);

    @ApiOperation(value="通过spuId查询detail数据")
    @GetMapping("/goods/spu/detail")
    Result<SpecDetailDTO> getSpecDetailBySpuId(Integer spuId);

    @GetMapping("/goods/spu/getSku")
    @ApiOperation(value = "通过spuId查询sku参数")
    Result<List<SpecSkuDTO>> getSkuBySpu(Integer spuId);
}
