package com.onetuks.goguma_bookstore.global.service.vo;

public abstract class File {

  private static final String BUCKET_URL = "https://test-bucket-url.com";

  public String getBucketUrl() {
    return BUCKET_URL;
  }
}
