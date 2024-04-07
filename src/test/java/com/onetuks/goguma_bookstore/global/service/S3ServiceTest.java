package com.onetuks.goguma_bookstore.global.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.onetuks.goguma_bookstore.IntegrationTest;
import java.io.File;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

public class S3ServiceTest extends IntegrationTest {

  @Autowired private S3Service s3Service;

  @Test
  @DisplayName("S3 파일 입출력에 성공한다.")
  public void s3PutAndGetTest() throws Exception {
    // Given
    String bucket = "test-bucket";
    String key = "api-docs";
    File sampleFile = new ClassPathResource("static/swagger.json").getFile();

    // When
    s3Service.putFile(bucket, key, sampleFile);

    // Then
    File resultFile = s3Service.getFile(bucket, key);

    List<String> sampleFileLines = FileUtils.readLines(sampleFile);
    List<String> resultFileLines = FileUtils.readLines(resultFile);

    assertIterableEquals(sampleFileLines, resultFileLines);
  }
}
