package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class S3ServiceTest extends IntegrationTest {

  @Autowired private S3Service s3Service;

  @Test
  @DisplayName("파일이 성공적으로 S3에 업로드된다.")
  void s3PutFileSuccessTest() {
    // Given
    long memberId = 1_0000L;
    CustomFile customFile = CustomFileFixture.createFile(memberId, FileType.PROFILES);

    // When
    s3Service.putFile(customFile);

    // Then
    File result = s3Service.getFile(customFile.getUri());

    assertAll(
        () -> assertThat(result).isFile(),
        () -> assertThat(result).hasSize(customFile.getMultipartFile().getSize()));
  }

  @Test
  @DisplayName("파일이 없는 경우 파일 저장 로직을 실행하지 않고 바로 메소드가 종료된다.")
  void s3PutFile_NullFile_ReturnTest() {
    // Given
    CustomFile customFile = CustomFileFixture.createNullFile();

    // When
    s3Service.putFile(customFile);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Service.getFile(customFile.getUri()));
  }

  @Test
  @DisplayName("S3에 있는 파일을 성공적으로 제거한다.")
  void s3DeleteFileSuccessTest() {
    // Given
    long memberId = 1L;
    CustomFile customFile = CustomFileFixture.createFile(memberId, FileType.PROFILES);
    s3Service.putFile(customFile);

    // When
    s3Service.deleteFile(customFile.getUri());

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Service.getFile(customFile.getUri()));
  }

  @Test
  @DisplayName("s3에 없는 파일을 제거하려고 할때 예외가 발생한다.")
  void S3Delete_NotExistFile_ExceptionTest() {
    // Given
    String uri = "not-exists-file-uri";

    // When
    s3Service.deleteFile(uri);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Service.getFile(uri));
  }
}
