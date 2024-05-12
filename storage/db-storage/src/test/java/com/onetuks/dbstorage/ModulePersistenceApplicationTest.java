package com.onetuks.dbstorage;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ModulePersistenceApplicationTest extends PersistenceIntegrationTest {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("ModulePersistenceApplicationTest");
  }
}
