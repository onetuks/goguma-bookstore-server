package com.onetuks.goguma_bookstore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class GogumaBookEntityStoreServerApplicationTest extends IntegrationTest {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("GogumaBookEntityStoreServerApplicationTest");
  }
}