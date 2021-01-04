package com.baidu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 2 * @ClassName RunXxxAplication
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/22
 * 6 * @Version V1.0
 * 7
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.baidu.shop.mapper")
public class RunXxxApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunXxxApplication.class,args);
    }
}
