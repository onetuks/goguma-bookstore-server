package com.onetuks.coreobj.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.coreobj.CoreObjIntegrationTest;
import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.MultipartFileFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class FileWrapperTest extends CoreObjIntegrationTest {

  @Test
  @DisplayName("파일이 없는 객체를 생성한다.")
  void of_Test() {
    // Given & When
    FileWrapper fileWrapper = FileWrapper.of();
    FileWrapper fileWrapper1 = FileWrapperFixture.createFile(FileType.PROFILES, UUIDProvider.provideUUID());

    // Then
    assertThat(fileWrapper.isNullFile()).isTrue();
    assertThat(fileWrapper1.isNullFile()).isFalse();
  }

  @Test
  @DisplayName("상세 이미지 파일이 4개 미만인 경우 예외를 던진다.")
  void of_LessThan4DetailImgFile_ExceptionTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.DETAILS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 2)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, uuid, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(fileType, uuid, multipartFiles));
  }

  @Test
  @DisplayName("파일 컬렉션 생성한다.")
  void of_Collection_Test() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.PREVIEWS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 5)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, uuid, index), fileType))
            .toArray(MultipartFile[]::new);

    // When
    FileWrapperCollection results =
        FileWrapperCollection.of(fileType, uuid, multipartFiles);

    // Then
    assertThat(results.isEmpty()).isFalse();
    assertThat(results.getUris()).isNotEmpty().allSatisfy(uri -> assertThat(uri).contains(uuid));
  }

  @Test
  @DisplayName("상세 이미지 파일이 10개 초과인 경우 예외를 던진다.")
  void of_GreaterThan10DetailImgFile_ExceptionTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.DETAILS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 15)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, uuid, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(fileType, uuid, multipartFiles));
  }

  @Test
  @DisplayName("미리보기 파일이 25개 미만인 경우 예외를 던진다.")
  void of_LessThan25PreviewFile_ExceptionTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.PREVIEWS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 20)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, uuid, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(fileType, uuid, multipartFiles));
  }

  @Test
  @DisplayName("리뷰 파일이 5개 초과인 경우 예외를 던진다.")
  void of_MorThan5ReviewFile_ExceptionTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileType fileType = FileType.REVIEWS;
    MultipartFile[] multipartFiles =
        IntStream.range(0, 20)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, uuid, index), fileType))
            .toArray(MultipartFile[]::new);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> FileWrapperCollection.of(fileType, uuid, multipartFiles));
  }

  @Test
  void equalsAndHashCodeTest() {
    // Given & When
    String uuid = UUIDProvider.provideUUID();
    FileWrapper file1 = FileWrapperFixture.createFile(FileType.PROFILES, uuid);
    FileWrapper file2 = FileWrapperFixture.createFile(FileType.PROFILES, uuid);

    // Then
    assertThat(file1.equals(file2)).isTrue();
    assertThat(file1).hasSameHashCodeAs(file2);
  }
}
