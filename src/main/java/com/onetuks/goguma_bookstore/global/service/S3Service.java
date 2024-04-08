package com.onetuks.goguma_bookstore.global.service;

import com.onetuks.goguma_bookstore.global.config.S3Config;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

  private final S3Client s3Client;
  private final S3Config s3Config;

  public S3Service(S3Client s3Client, S3Config s3Config) {
    this.s3Client = s3Client;
    this.s3Config = s3Config;
  }

  public void putFile(String uri, MultipartFile file) {
    try {
      s3Client.putObject(
          PutObjectRequest.builder().bucket(s3Config.getBucketName()).key(uri).build(),
          RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public File getFile(String uri) {
    File file = new File("build/output/" + uri);

    try {
      ResponseInputStream<GetObjectResponse> res =
          s3Client.getObject(
              GetObjectRequest.builder().bucket(s3Config.getBucketName()).key(uri).build());

      FileUtils.writeByteArrayToFile(file, res.readAllBytes());

      return file;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void deleteFile(String uri) {
    s3Client.deleteObject(
        DeleteObjectRequest.builder().bucket(s3Config.getBucketName()).key(uri).build());
  }
}