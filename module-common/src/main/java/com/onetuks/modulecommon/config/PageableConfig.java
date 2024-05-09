package com.onetuks.modulecommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

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
