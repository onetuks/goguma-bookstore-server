package com.onetuks.modulereader.config;

import com.onetuks.modulecommon.config.CommonProviderConfig;
import com.onetuks.modulepersistence.global.config.PersistenceProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PersistenceProviderConfig.class, CommonProviderConfig.class})
public class BeanInjectionConfig {}
