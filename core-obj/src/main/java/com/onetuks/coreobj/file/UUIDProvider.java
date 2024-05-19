package com.onetuks.coreobj.file;

import java.util.UUID;

public class UUIDProvider {

  public static String provideUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
