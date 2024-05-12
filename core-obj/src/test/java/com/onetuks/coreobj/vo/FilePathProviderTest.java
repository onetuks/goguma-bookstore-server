package com.onetuks.coreobj.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.coreobj.CoreObjIntegrationTest;
import com.onetuks.coreobj.enums.file.FileType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FilePathProviderTest extends CoreObjIntegrationTest {

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
