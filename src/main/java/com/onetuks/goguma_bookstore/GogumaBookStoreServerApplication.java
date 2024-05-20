package com.onetuks.goguma_bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.onetuks")
public class GogumaBookStoreServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(GogumaBookStoreServerApplication.class, args);
  }
}


