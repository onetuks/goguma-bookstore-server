package com.onetuks.readerdomain;

import com.onetuks.readerdomain.ReaderDomainIntegrationTest.ReaderDomainConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = ReaderDomainConfig.class)
@Transactional
public class ReaderDomainIntegrationTest {

  @Configuration
  @ComponentScan(basePackages = {"com.onetuks.readerdomain", "com.onetuks.coredomain"})
  public static class ReaderDomainConfig {}
}
