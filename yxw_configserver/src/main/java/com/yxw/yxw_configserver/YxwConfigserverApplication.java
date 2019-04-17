package com.yxw.yxw_configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableConfigServer
@SpringBootApplication
public class YxwConfigserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxwConfigserverApplication.class, args);
    }

}
