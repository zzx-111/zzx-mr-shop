package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "操作模板")
public interface TemplateService {

    @GetMapping(value = "template/createStaticHTMLTemplate")
    @ApiOperation(value = "通过spuId创建模板")
    Result<JSONObject> createStaticHTMLTemplate(Integer spuId);

    @ApiOperation(value = "初始化所有模板")
    @GetMapping(value = "template/initStaticHTMLTemplate")
    Result<JSONObject> initStaticHTMLTemplate();

    @ApiOperation(value = "清除所有模板")
    @GetMapping(value = "template/clearStaticHTMLTemplate")
    Result<JSONObject> clearStaticHTMLTemplate();

    @ApiOperation(value = "通过spuId清除模板")
    @GetMapping(value = "template/deleteStaticHTMLTemplate")
    Result<JSONObject> deleteStaticHTMLTemplate(Integer spuId);
}
