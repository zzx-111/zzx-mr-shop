package com.baidu.shop.feign;

import com.baidu.shop.service.CategoryService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 2 * @ClassName CategoryFeign
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/6
 * 6 * @Version V1.0
 * 7
 **/
@FeignClient(value="xxx-server",contextId = "CategoryService")
public interface CategoryFeign  extends CategoryService {
}
