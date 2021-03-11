package com.baidu.shop.feign;

import com.baidu.shop.service.BrandService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 2 * @ClassName BrandFeign
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/6
 * 6 * @Version V1.0
 * 7
 **/
@FeignClient(value = "xxx-server", contextId = "BrandService")
public interface BrandFeign   extends BrandService {
}
