package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.GoodsDTO;
import com.baidu.shop.entity.GoodsEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

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
}
