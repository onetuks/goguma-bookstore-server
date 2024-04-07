package com.onetuks.goguma_bookstore.global.service;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

  private final S3Client s3Client;

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public void putFile(String bucket, String key, File file) {
    s3Client.putObject(
        PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(file));
  }

  public File getFile(String bucket, String key) {
    File file = new File("build/output/api-docs.json");

    ResponseInputStream<GetObjectResponse> res =
        s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());

    try {
      FileUtils.writeByteArrayToFile(file, res.readAllBytes());

      return file;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
