package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.goguma_bookstore.IntegrationTest;
import java.io.File;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

class S3ServiceTest extends IntegrationTest {

  @Autowired private S3Service s3Service;

  @Test
  @DisplayName("파일이 성공적으로 S3에 업로드된다.")
  void s3PutFileSuccessTest() throws Exception {
    // Given
    String bucket = "test-bucket";
    String key = "api-docs";
    File expected = new ClassPathResource("static/swagger.json").getFile();

    // When
    s3Service.putFile(bucket, key, expected);

    // Then
    File result = s3Service.getFile(bucket, key);

    assertThat(result).hasSize(expected.length());
  }

  @Test
  @DisplayName("존재하지 않는 파일을 업로드하려고 하면 예외가 발생한다.")
  void s3PutNonExistentFileTest() {
    // Given
    String bucket = "test-bucket";
    String key = "non-existent-file";
    File nonExistentFile = new File("non-existent-file");

    // When & Then
    assertThatThrownBy(() -> s3Service.putFile(bucket, key, nonExistentFile))
        .isInstanceOf(UncheckedIOException.class);
  }
}
