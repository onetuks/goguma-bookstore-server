package com.onetuks.filestorage.repository;

import com.onetuks.coredomain.global.file.repository.FileRepository;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.filestorage.config.S3Config;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Repository
public class S3Repository implements FileRepository {

  private final S3Client s3Client;
  private final S3Config s3Config;

  public S3Repository(S3Client s3Client, S3Config s3Config) {
    this.s3Client = s3Client;
    this.s3Config = s3Config;
  }

  @Override
  public void putFile(FileWrapper file) {
    if (file.isNullFile()) {
      return;
    }

    try {
      s3Client.putObject(
          PutObjectRequest.builder().bucket(s3Config.getBucketName()).key(file.getUri()).build(),
          RequestBody.fromInputStream(
              file.getMultipartFile().getInputStream(), file.getMultipartFile().getSize()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public File getFile(String uri) {
    File file = new File("src/test/resources/static" + uri);

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

  @Override
  public void deleteFile(String uri) {
    try {
      s3Client.deleteObject(
          DeleteObjectRequest.builder().bucket(s3Config.getBucketName()).key(uri).build());
    } catch (NoSuchKeyException e) {
      // 이미 삭제된 파일이므로 무시
    }
  }
}
