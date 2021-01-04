package com.baidu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 2 * @ClassName RunUploadServerApplication
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/29
 * 6 * @Version V1.0
 * 7
 **/
@SpringBootApplication
@EnableEurekaClient
public class RunUploadServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunUploadServerApplication.class,args);
    }
}
