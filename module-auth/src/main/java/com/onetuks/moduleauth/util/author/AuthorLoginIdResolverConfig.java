package com.onetuks.moduleauth.util.author;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthorLoginIdResolverConfig implements WebMvcConfigurer {

  private final AuthorLoginIdResolver resolver;

  public AuthorLoginIdResolverConfig(AuthorLoginIdResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(resolver);
  }
}
