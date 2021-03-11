package com.baidu.shop.service;

import java.util.Map;

/**
 * 2 * @ClassName PageService
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/8
 * 6 * @Version V1.0
 * 7
 **/
public interface PageService {
    Map<String, Object> getGoodsInfo(Integer spuId);
}
