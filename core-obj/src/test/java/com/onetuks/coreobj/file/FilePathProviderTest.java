package com.onetuks.coreobj.file;

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
    String uuid = UUIDProvider.provideUUID();

    // When
    String result = FilePathProvider.provideFileURI(fileType, uuid);

    // Then
    assertThat(result).isEqualTo(fileType.getDirectoryPath() + uuid + fileType.getFileExtension());
  }
}
