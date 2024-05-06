package com.onetuks.modulereader.config;

import com.onetuks.modulecommon.config.CommonBeanProviderConfig;
import com.onetuks.modulepersistence.global.config.PersistenceBeanProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PersistenceBeanProviderConfig.class, CommonBeanProviderConfig.class})
public class BeanInjectionConfig {}
