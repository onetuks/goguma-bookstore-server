package com.onetuks.coreauth.util.admin;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminLoginIdResolverConfig implements WebMvcConfigurer {

  private final AdminLoginIdResolver resolver;

  public AdminLoginIdResolverConfig(AdminLoginIdResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(resolver);
  }
}
