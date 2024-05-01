package com.onetuks.moduleauth.config;

import com.onetuks.modulepersistence.global.config.PersistenceProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PersistenceProviderConfig.class})
public class AuthBeanInjectionConfig {}
