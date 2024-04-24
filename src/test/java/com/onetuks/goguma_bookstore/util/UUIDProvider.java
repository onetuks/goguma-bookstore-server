package com.onetuks.goguma_bookstore.util;

import java.util.UUID;

public class UUIDProvider {

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
