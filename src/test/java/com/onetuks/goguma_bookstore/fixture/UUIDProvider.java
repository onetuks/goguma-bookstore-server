package com.onetuks.goguma_bookstore.fixture;

import java.util.UUID;

public class UUIDProvider {

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
