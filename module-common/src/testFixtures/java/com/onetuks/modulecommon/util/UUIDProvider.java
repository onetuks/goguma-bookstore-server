package com.onetuks.modulecommon.util;

import java.util.UUID;

public class UUIDProvider {

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
