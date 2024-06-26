package com.onetuks.coreauth.util.author;

import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackageClasses = AuthorIdResolver.class)
public class AuthorIdResolverConfig implements WebMvcConfigurer {

  private final AuthorIdResolver resolver;

  public AuthorIdResolverConfig(AuthorIdResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(resolver);
  }
}
