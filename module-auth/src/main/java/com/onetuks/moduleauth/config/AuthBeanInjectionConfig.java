package com.onetuks.moduleauth.config;

import com.onetuks.modulepersistence.global.config.PersistenceBeanProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PersistenceBeanProviderConfig.class})
public class AuthBeanInjectionConfig {}
