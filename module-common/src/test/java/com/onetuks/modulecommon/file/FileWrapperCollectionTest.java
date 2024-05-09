package com.onetuks.modulecommon.file;

import static org.assertj.core.api.Assertions.*;

import com.onetuks.modulecommon.CommonIntegrationTest;
import com.onetuks.modulecommon.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulecommon.file.provider.FilePathProvider;
import com.onetuks.modulecommon.fixture.MultipartFileFixture;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class FileWrapperCollectionTest extends CommonIntegrationTest {

  @Test
  @DisplayName("여러개의 멀티파트파일을 묶어 하나의 파일 래퍼 컬렉션으로 생성한다.")
  void ofTest() {
    // Given
    long id = 123_412_415L;
    FileType fileType = FileType.DETAILS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 10)
            .mapToObj(
                i ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileURI(fileType, id), fileType))
            .toArray(MultipartFile[]::new);

    // When
    FileWrapperCollection results = FileWrapperCollection.of(id, fileType, multipartFiles);

    // Then
    assertThat(results.fileWrappers())
        .hasSize(10)
        .allSatisfy(
            result -> {
              assertThat(result.getUri()).contains(String.valueOf(id));
              assertThat(result.getMultipartFile()).isNotNull();
            });
  }

  @Test
  @DisplayName("멀티파일이 주어지지 않은 상태에서 파일 래퍼 컬렉션을 생성하면 빈 컬렉션을 만든다.")
  void ofEmptyTest() {
    // Given
    long id = 123_412_415L;
    FileType fileType = FileType.DETAILS;

    // When
    FileWrapperCollection results = FileWrapperCollection.of(id, fileType, null);

    // Then
    assertThat(results.fileWrappers()).isEmpty();
  }
}
