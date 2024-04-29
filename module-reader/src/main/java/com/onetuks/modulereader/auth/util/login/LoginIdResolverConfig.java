package com.onetuks.modulereader.auth.util.login;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginIdResolverConfig implements WebMvcConfigurer {

  private final LoginIdResolver resolver;

  public LoginIdResolverConfig(LoginIdResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(resolver);
  }
}
