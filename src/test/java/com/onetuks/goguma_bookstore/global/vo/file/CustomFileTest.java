package com.onetuks.goguma_bookstore.global.vo.file;

import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.vo.file.provider.FilePathProvider;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class CustomFileTest extends IntegrationTest {

  private final long id = 1L;

  @Test
  @DisplayName("목업 파일이 4개 미만인 경우 예외를 던진다.")
  void of_LessThan4MockUpFile_ExceptionTest() {
    // Given
    FileType fileType = FileType.DETAILS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 2)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class, () -> CustomFile.of(id, FileType.DETAILS, multipartFiles));
  }

  @Test
  @DisplayName("목업 파일이 10개 초과인 경우 예외를 던진다.")
  void of_GreaterThan10MockUpFile_ExceptionTest() {
    // Given
    FileType fileType = FileType.DETAILS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 15)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class, () -> CustomFile.of(id, FileType.DETAILS, multipartFiles));
  }

  @Test
  @DisplayName("미리보기 파일이 25개 미만인 경우 예외를 던진다.")
  void of_LessThan25PreviewFile_ExceptionTest() {
    // Given
    FileType fileType = FileType.PREVIEWS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 20)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class, () -> CustomFile.of(id, FileType.PREVIEWS, multipartFiles));
  }
}
