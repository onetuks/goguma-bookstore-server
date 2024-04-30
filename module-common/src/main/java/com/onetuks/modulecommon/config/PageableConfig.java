package com.onetuks.modulecommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

  @Bean
  public PageableHandlerMethodArgumentResolverCustomizer customize() {
    return p -> {
      p.setOneIndexedParameters(true);
      p.setMaxPageSize(10);
    };
  }
}
