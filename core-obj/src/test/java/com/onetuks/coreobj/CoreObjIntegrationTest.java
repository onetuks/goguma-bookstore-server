package com.onetuks.coreobj;

import com.onetuks.coreobj.CoreObjIntegrationTest.CoreObjConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = CoreObjConfig.class)
public class CoreObjIntegrationTest {

  @Configuration
  @ComponentScan(basePackages = "com.onetuks.coreobj")
  public static class CoreObjConfig {

  }
}
