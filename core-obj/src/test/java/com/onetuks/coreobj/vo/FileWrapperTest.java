package com.onetuks.coreobj.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.MultipartFileFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.vo.FileWrapper.FileWrapperCollection;
import com.onetuks.coreobj.CoreObjIntegrationTest;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileWrapperTest extends CoreObjIntegrationTest {

  private final long id = 1L;

  @Test
  @DisplayName("상세 이미지 파일이 4개 미만인 경우 예외를 던진다.")
  void of_LessThan4DetailImgFile_ExceptionTest() {
    // Given
    FileType fileType = FileType.DETAILS;
    org.springframework.web.multipart.MultipartFile[] multipartFiles =
        IntStream.range(0, 2)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(org.springframework.web.multipart.MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(id, FileType.DETAILS, multipartFiles));
  }

  @Test
  @DisplayName("상세 이미지 파일이 10개 초과인 경우 예외를 던진다.")
  void of_GreaterThan10DetailImgFile_ExceptionTest() {
    // Given
    FileType fileType = FileType.DETAILS;
    org.springframework.web.multipart.MultipartFile[] multipartFiles =
        IntStream.range(0, 15)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(org.springframework.web.multipart.MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(id, FileType.DETAILS, multipartFiles));
  }

  @Test
  @DisplayName("미리보기 파일이 25개 미만인 경우 예외를 던진다.")
  void of_LessThan25PreviewFile_ExceptionTest() {
    // Given
    FileType fileType = FileType.PREVIEWS;
    org.springframework.web.multipart.MultipartFile[] multipartFiles =
        IntStream.range(0, 20)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(org.springframework.web.multipart.MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(id, FileType.PREVIEWS, multipartFiles));
  }

  @Test
  void equalsAndHashCodeTest() {
    // Given & When
    FileWrapper file1 = FileWrapperFixture.createFile(id, FileType.PROFILES);
    FileWrapper file2 = FileWrapperFixture.createFile(id, FileType.PROFILES);

    // Then
    assertThat(file1.equals(file2)).isTrue();
    assertThat(file1).hasSameHashCodeAs(file2);
  }
}
