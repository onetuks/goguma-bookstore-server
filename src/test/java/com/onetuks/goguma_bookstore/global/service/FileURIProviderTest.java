package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FileURIProviderTest extends IntegrationTest {

  @Autowired private FileURIProvider fileURIProvider;

  @Test
  @DisplayName("프로필 이미지 URI를 생성한다.")
  void provideFileURITest() {
    // Given
    FileType fileType = FileType.PROFILES;
    Long id = 12341234L;

    // When
    String result = fileURIProvider.provideFileURI(fileType, id);

    // Then
    assertThat(result).isEqualTo(fileType.getDirectoryPath() + id + fileType.getFileExtension());
  }
}
