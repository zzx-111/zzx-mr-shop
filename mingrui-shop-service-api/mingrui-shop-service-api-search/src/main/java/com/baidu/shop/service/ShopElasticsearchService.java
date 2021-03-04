package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 2 * @ClassName ShopElasticsearchService
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/4
 * 6 * @Version V1.0
 * 7
 **/
@Api(tags = "elasticsearch接口")
public interface ShopElasticsearchService {

    @ApiOperation(value = "获取商品信息测试")
    @GetMapping(value = "es/goodsInfo")
    Result<JSONObject> esGoodsInfo();
}
