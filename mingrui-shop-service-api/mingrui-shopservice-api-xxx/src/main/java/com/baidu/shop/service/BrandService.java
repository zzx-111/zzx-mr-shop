package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "品牌管理")
public interface BrandService {

    @GetMapping("/brand/list")
    @ApiOperation(value ="品牌管理查询")
    Result<List<BrandEntity>> getBrandList(BrandDTO brandDTO);

    @PostMapping("/brand/save")
    @ApiOperation(value ="品牌新增")
    Result<JsonObject> saveBrand(@RequestBody BrandDTO brandDTO);

    @PutMapping("/brand/save")
    @ApiOperation(value ="品牌修改")
    Result<JsonObject> editBrand(@RequestBody BrandDTO brandDTO);


    @DeleteMapping("/brand/delete")
    @ApiOperation(value ="品牌删除")
    Result<JsonObject> deleteBrand(Integer id);

}
