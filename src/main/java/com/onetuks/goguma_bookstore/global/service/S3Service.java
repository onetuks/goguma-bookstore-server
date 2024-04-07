package com.onetuks.goguma_bookstore.global.service;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Service
public class S3Service {

  private static final Logger log = LoggerFactory.getLogger(S3Service.class);
  private final S3Client s3Client;

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public void putFile(String bucket, String key, File file) {
    s3Client.putObject(
        req -> {
          req.bucket(bucket);
          req.key(key);
        },
        RequestBody.fromFile(file));
  }

  public File getFile(String bucket, String key) {
    File file = new File("build/output/getFile.txt");

    ResponseInputStream<GetObjectResponse> res =
        s3Client.getObject(
            req -> {
              req.bucket(bucket);
              req.key(key);
            });

    try {
      FileUtils.writeByteArrayToFile(file, res.readAllBytes());

      return file;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
