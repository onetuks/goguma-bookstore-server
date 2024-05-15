package com.onetuks.scmdomain;

import com.onetuks.scmdomain.ScmDomainIntegrationTest.ScmDomainConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = ScmDomainConfig.class)
@Transactional
public class ScmDomainIntegrationTest {

  @Configuration
  @ComponentScan(basePackages = {"com.onetuks.scmdomain", "com.onetuks.coredomain"})
  public static class ScmDomainConfig {}
}
