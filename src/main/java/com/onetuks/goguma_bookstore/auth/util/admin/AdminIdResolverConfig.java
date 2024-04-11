package com.onetuks.goguma_bookstore.auth.util.admin;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminIdResolverConfig implements WebMvcConfigurer {

  private final AdminIdResolver resolver;

  public AdminIdResolverConfig(AdminIdResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(resolver);
  }
}
