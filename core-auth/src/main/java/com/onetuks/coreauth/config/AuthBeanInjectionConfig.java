package com.onetuks.coreauth.config;

import com.onetuks.dbstorage.global.config.PersistenceBeanProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PersistenceBeanProviderConfig.class})
public class AuthBeanInjectionConfig {}
