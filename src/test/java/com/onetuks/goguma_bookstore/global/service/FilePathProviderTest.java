package com.onetuks.goguma_bookstore.global.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.provider.FilePathProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FilePathProviderTest extends IntegrationTest {

  @Test
  @DisplayName("프로필 이미지 URI 를 생성한다.")
  void provideFileURITest() {
    // Given
    FileType fileType = FileType.PROFILES;
    Long id = 12341234L;

    // When
    String result = FilePathProvider.provideFileURI(fileType, id);

    // Then
    assertThat(result).isEqualTo(fileType.getDirectoryPath() + id + fileType.getFileExtension());
  }
}
