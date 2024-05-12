package com.onetuks.coredomain.util;

import java.util.UUID;

public class UUIDProvider {

  protected static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
