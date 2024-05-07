package com.onetuks.modulepersistence.global.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.onetuks.modulepersistence")
@EntityScan(basePackages = "com.onetuks.modulepersistence")
@ComponentScan(basePackages = "com.onetuks.modulepersistence")
public class PersistenceBeanProviderConfig {}