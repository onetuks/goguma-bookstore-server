package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.goguma_bookstore.IntegrationTest;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class S3ServiceTest extends IntegrationTest {

  @Autowired private S3Service s3Service;

  @Test
  @DisplayName("파일이 성공적으로 S3에 업로드된다.")
  void s3PutFileSuccessTest() throws Exception {
    // Given
    String uri = "sample.txt";
    File expected = new ClassPathResource("static/sample.txt").getFile();

    // When
    s3Service.putFile(uri, expected);

    // Then
    File result = s3Service.getFile(uri);

    assertThat(result).hasSize(expected.length());
  }

  @Test
  @DisplayName("존재하지 않는 파일을 업로드하려고 하면 예외가 발생한다.")
  void s3PutNonExistentFileTest() {
    // Given
    String key = "non-existent-file";
    File nonExistentFile = new File("non-existent-file");

    // When & Then
    assertThatThrownBy(() -> s3Service.putFile(key, nonExistentFile))
        .isInstanceOf(UncheckedIOException.class);
  }

  @Test
  @DisplayName("S3에 있는 파일을 성공적으로 제거한다.")
  void s3DeleteFileSuccessTest() throws IOException {
    // Given
    String uri = "sample.txt";
    File expected = new ClassPathResource("static/sample.txt").getFile();
    s3Service.putFile(uri, expected);

    // When
    s3Service.deleteFile(uri);

    // Then
    assertThatThrownBy(() -> s3Service.getFile(uri)).isInstanceOf(NoSuchKeyException.class);
  }

  @Test
  @DisplayName("s3에 없는 파일을 제거하려고 할때 예외가 발생한다.")
  void S3Delete_NotExistFile_ExceptionTest() {
    // Given
    String uri = "sample.txt";

    // When
    s3Service.deleteFile(uri);

    // Then
    assertThatThrownBy(() -> s3Service.getFile(uri)).isInstanceOf(NoSuchKeyException.class);
  }
}
