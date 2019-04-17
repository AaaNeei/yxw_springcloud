package com.yxw.yxw_euerka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class YxwEuerkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxwEuerkaApplication.class, args);
    }

}
