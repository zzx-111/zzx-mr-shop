package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 2 * @ClassName CategoryService
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/22
 * 6 * @Version V1.0
 * 7
 **/
@Api(tags = "分类接口")//@Api作用于接口上.value无法支持中文字体
public interface CategoryService {

    @ApiOperation(value="通过pid查询分类列表")
    @GetMapping("category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value ="通过id删除数据")
    @DeleteMapping("category/delete")
    Result<JSONObject> deleteCategoryById( Integer id);

    @ApiOperation(value="修改数据")
    @PutMapping("category/edit")
    Result<JsonObject> editCategoryById(@Validated({ MingruiOperation.update.class }) @RequestBody CategoryEntity categoryEntity);


    @ApiOperation(value="新增数据")
    @PostMapping("category/save")
    Result<JsonObject> saveCategoryById(@Validated({ MingruiOperation.add.class }) @RequestBody CategoryEntity categoryEntity);


    @GetMapping("category/getBrandByBrandId")
    @ApiOperation(value ="根据品牌id查询分类信息")
    Result<List<CategoryEntity>> getBrandByBrandId(Integer brandId);

    @GetMapping("category/getCategoryListById")
    @ApiOperation(value ="根据id查询分类信息")
    Result<List<CategoryEntity>>  getCategoryListById(@RequestParam String ids);



}
