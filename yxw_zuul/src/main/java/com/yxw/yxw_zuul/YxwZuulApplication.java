package com.yxw.yxw_zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@EnableZuulProxy
@SpringBootApplication
public class YxwZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxwZuulApplication.class, args);
    }

}
