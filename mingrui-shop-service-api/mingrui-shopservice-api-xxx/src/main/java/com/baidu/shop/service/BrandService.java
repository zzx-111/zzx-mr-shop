package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "品牌管理")
public interface BrandService {

    @GetMapping("/brand/list")
    @ApiOperation(value ="品牌管理查询")
    Result<PageInfo<BrandEntity>>  getBrandList(@SpringQueryMap BrandDTO brandDTO);

    @PostMapping("/brand/save")
    @ApiOperation(value ="品牌新增")
    Result<JsonObject> saveBrand(@Validated( {MingruiOperation.add.class})   @RequestBody BrandDTO brandDTO);

    @PutMapping("/brand/save")
    @ApiOperation(value ="品牌修改")
    Result<JsonObject> editBrand( @Validated( {MingruiOperation.update.class})  @RequestBody BrandDTO brandDTO);


    @DeleteMapping("/brand/delete")
    @ApiOperation(value ="品牌删除")
    Result<JsonObject> deleteBrand(Integer id);


    @GetMapping("/brand/getBrandNameByCid")
    @ApiOperation(value ="品牌管理查询")
    Result<List<BrandEntity>> getBrandNameByCid(Integer cid);

    @GetMapping("/brand/getBrandListById")
    @ApiOperation(value ="通过id品牌管理查询")
    Result<List<BrandEntity>> getBrandListById(@RequestParam String join);
}
