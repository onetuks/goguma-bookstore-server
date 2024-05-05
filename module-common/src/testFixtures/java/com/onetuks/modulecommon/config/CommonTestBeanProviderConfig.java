package com.onetuks.modulecommon.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "com.onetuks.modulecommon.util")
public class CommonTestBeanProviderConfig {}
