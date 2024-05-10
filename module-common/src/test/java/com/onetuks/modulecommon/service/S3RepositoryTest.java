package com.onetuks.modulecommon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.CommonIntegrationTest;
import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class S3RepositoryTest extends CommonIntegrationTest {

  @Autowired private S3Repository s3Repository;

  @Test
  @DisplayName("파일이 성공적으로 S3에 업로드된다.")
  void s3PutFileSuccessTest() {
    // Given
    long memberId = 1_0000L;
    FileWrapper fileWrapper = FileWrapperFixture.createFile(memberId, FileType.PROFILES);

    // When
    s3Repository.putFile(fileWrapper);

    // Then
    File result = s3Repository.getFile(fileWrapper.getUri());

    assertAll(
        () -> assertThat(result).isFile(),
        () -> assertThat(result).hasSize(fileWrapper.getMultipartFile().getSize()));
  }

  @Test
  @DisplayName("파일이 없는 경우 파일 저장 로직을 실행하지 않고 바로 메소드가 종료된다.")
  void s3PutFile_NullFile_ReturnTest() {
    // Given
    FileWrapper fileWrapper = FileWrapperFixture.createNullFile();

    // When
    s3Repository.putFile(fileWrapper);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(fileWrapper.getUri()));
  }

  @Test
  @DisplayName("S3에 있는 파일을 성공적으로 제거한다.")
  void s3DeleteFileSuccessTest() {
    // Given
    long memberId = 1L;
    FileWrapper fileWrapper = FileWrapperFixture.createFile(memberId, FileType.PROFILES);
    s3Repository.putFile(fileWrapper);

    // When
    s3Repository.deleteFile(fileWrapper.getUri());

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(fileWrapper.getUri()));
  }

  @Test
  @DisplayName("s3에 없는 파일을 제거하려고 할때 예외가 발생한다.")
  void S3Delete_NotExistFile_ExceptionTest() {
    // Given
    String uri = "not-exists-file-uri";

    // When
    s3Repository.deleteFile(uri);

    // Then
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(uri));
  }
}
