package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "规格接口")
public interface SpecificationService   {


    @ApiOperation(value = "规格列表查询")
    @GetMapping("/spec/list")
    Result<List<SpecGroupEntity>>  getSpecList(SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "规格新增")
    @PostMapping("/spec/save")
    Result<JsonObject>  saveSpec(@RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "规格修改")
    @PutMapping("/spec/save")
    Result<JsonObject>  editSpec(@RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value = "规格列表查询")
    @DeleteMapping("/spec/delete")
    Result<JsonObject>  deleteSpec(Integer id);


    @ApiOperation(value = "规格参数列表查询")
    @GetMapping("/specParam/list")
    Result<List<SpecGroupEntity>>  getSpecParamList(SpecParamDTO specParamDTO);

    @ApiOperation(value = "规格参数新增")
    @PostMapping("/specParam/save")
    Result<JsonObject>  saveSpecParam(@RequestBody SpecParamDTO specParamDTO);

    @ApiOperation(value = "规格参数修改")
    @PutMapping("/specParam/save")
    Result<JsonObject>  editSpecParam(@RequestBody SpecParamDTO specParamDTO);

    @ApiOperation(value = "规格参数删除")
    @DeleteMapping("/specParam/delete")
    Result<JsonObject>  deleteSpecParam(Integer id);


}
