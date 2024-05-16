package com.onetuks.readerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageableConfig {

  @Bean
  public PageableHandlerMethodArgumentResolverCustomizer customize() {
    return pageConfig -> {
      pageConfig.setOneIndexedParameters(true);
      pageConfig.setMaxPageSize(10);
    };
  }
}
