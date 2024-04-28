package com.onetuks.goguma_bookstore.global.config;

import com.onetuks.modulepersistence.global.config.PersistenceProviderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceProviderConfig.class)
public class PersistenceConfig {}
