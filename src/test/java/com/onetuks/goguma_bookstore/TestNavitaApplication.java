package com.onetuks.goguma_bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestNavitaApplication {

    public static void main(String[] args) {
        SpringApplication.from(GogumaBookStoreServerApplication::main).with(TestNavitaApplication.class).run(args);
    }

}
