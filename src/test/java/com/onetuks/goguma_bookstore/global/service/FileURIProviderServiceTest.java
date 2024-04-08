package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FileURIProviderServiceTest extends IntegrationTest {

  @Autowired private FileURIProviderService fileURIProviderService;

  @Test
  @DisplayName("프로필 이미지 URI를 생성한다.")
  void provideFileURITest() {
    // Given
    FileType fileType = FileType.PROFILES;
    Long id = 12341234L;

    // When
    String result = fileURIProviderService.provideFileURI(fileType, id);

    // Then
    assertThat(result).isEqualTo(fileType.getDirectoryPath() + id + fileType.getFileExtension());
  }
}
