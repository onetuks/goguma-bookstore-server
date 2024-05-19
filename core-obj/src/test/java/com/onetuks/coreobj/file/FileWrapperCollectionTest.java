package com.onetuks.coreobj.file;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.coreobj.CoreObjIntegrationTest;
import com.onetuks.coreobj.MultipartFileFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class FileWrapperCollectionTest extends CoreObjIntegrationTest {

  @Test
  @DisplayName("여러개의 멀티파트파일을 묶어 하나의 파일 래퍼 컬렉션으로 생성한다.")
  void ofTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.DETAILS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 10)
            .mapToObj(
                i ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileURI(fileType, uuid), fileType))
            .toArray(MultipartFile[]::new);

    // When
    FileWrapperCollection results = FileWrapperCollection.of(fileType, uuid, multipartFiles);

    // Then
    assertThat(results.fileWrappers())
        .hasSize(10)
        .allSatisfy(
            result -> {
              assertThat(result.getUri()).contains(uuid);
              assertThat(result.getMultipartFile()).isNotNull();
            });
  }

  @Test
  @DisplayName("멀티파일이 주어지지 않은 상태에서 파일 래퍼 컬렉션을 생성하면 빈 컬렉션을 만든다.")
  void ofEmptyTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.DETAILS;

    // When
    FileWrapperCollection results = FileWrapperCollection.of(fileType, uuid, null);

    // Then
    assertThat(results.fileWrappers()).isEmpty();
  }
}
