package com.yxw.yxw_rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class YxwRocketmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxwRocketmqApplication.class, args);
    }

}
