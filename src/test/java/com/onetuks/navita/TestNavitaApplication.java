package com.onetuks.navita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestNavitaApplication {

    public static void main(String[] args) {
        SpringApplication.from(NavitaApplication::main).with(TestNavitaApplication.class).run(args);
    }

}
