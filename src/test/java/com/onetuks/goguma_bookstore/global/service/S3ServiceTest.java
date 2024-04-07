package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class S3ServiceTest extends IntegrationTest {

  @Autowired private S3Service s3Service;

  @Test
  @DisplayName("파일이 성공적으로 S3에 업로드된다.")
  void s3PutFileSuccessTest() throws IOException {
    // Given
    MultipartFile expected = MultipartFileFixture.createFile(FileType.PROFILES);

    // When
    s3Service.putFile(expected.getName(), expected);

    // Then
    File result = s3Service.getFile(expected.getName());

    assertThat(result).hasSize(expected.getSize());
  }

  //  @Test
  //  @DisplayName("존재하지 않는 파일을 업로드하려고 하면 예외가 발생한다.")
  //  void s3PutNonExistentFileTest() throws IOException {
  //    // Given
  //    MultipartFile nonExistentFile = MultipartFileFixture.createFile("non-existent-file");
  //
  //    // When & Then
  //    assertThatThrownBy(() -> s3Service.putFile(nonExistentFile.getName(), nonExistentFile))
  //        .isInstanceOf(UncheckedIOException.class);
  //  }

  @Test
  @DisplayName("S3에 있는 파일을 성공적으로 제거한다.")
  void s3DeleteFileSuccessTest() throws IOException {
    // Given
    MultipartFile expected = MultipartFileFixture.createFile(FileType.PROFILES);
    s3Service.putFile(expected.getName(), expected);

    // When
    s3Service.deleteFile(expected.getName());

    // Then
    assertThatThrownBy(() -> s3Service.getFile(expected.getName()))
        .isInstanceOf(NoSuchKeyException.class);
  }

  @Test
  @DisplayName("s3에 없는 파일을 제거하려고 할때 예외가 발생한다.")
  void S3Delete_NotExistFile_ExceptionTest() {
    // Given
    String uri = MockMultipartFileInfo.ESCROW.getFileName();

    // When
    s3Service.deleteFile(uri);

    // Then
    assertThatThrownBy(() -> s3Service.getFile(uri)).isInstanceOf(NoSuchKeyException.class);
  }
}
