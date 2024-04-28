package com.onetuks.modulepersistence.global.vo.file;

import jakarta.persistence.Transient;

public interface CustomFilePath {

  String BUCKET_URL = "https://test-bucket-url.com";

  String getUri();

  String getUrl();
}
