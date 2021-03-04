package com.baidu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 2 * @ClassName RunEsServerApplication
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/4
 * 6 * @Version V1.0
 * 7
 **/
//将自动配置数据源 剔除
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients
public class RunEsServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(RunEsServerApplication.class,args);
    }
}
