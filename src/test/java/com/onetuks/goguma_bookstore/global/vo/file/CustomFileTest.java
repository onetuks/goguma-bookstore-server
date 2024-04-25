package com.onetuks.goguma_bookstore.global.vo.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
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

  @Test
  void equalsAndHashcodeTest() {
    // Given & When
    CoverImgFile coverImgFile1 = CustomFileFixture.createFile(1L, FileType.COVERS).toCoverImgFile();
    CoverImgFile coverImgFile2 = CustomFileFixture.createFile(1L, FileType.COVERS).toCoverImgFile();
    DetailImgFile detailImgFile1 =
        CustomFileFixture.createFile(1L, FileType.DETAILS).toDetailImgFile();
    DetailImgFile detailImgFile2 =
        CustomFileFixture.createFile(1L, FileType.DETAILS).toDetailImgFile();
    PreviewFile previewFile1 = CustomFileFixture.createFile(1L, FileType.PREVIEWS).toPreviewFile();
    PreviewFile previewFile2 = CustomFileFixture.createFile(1L, FileType.PREVIEWS).toPreviewFile();
    ProfileImgFile profileImgFile1 =
        CustomFileFixture.createFile(1L, FileType.PROFILES).toProfileImgFile();
    ProfileImgFile profileImgFile2 =
        CustomFileFixture.createFile(1L, FileType.PROFILES).toProfileImgFile();
    ReviewImgFile reviewImgFile1 =
        CustomFileFixture.createFile(1L, FileType.REVIEWS).toReivewFile();
    ReviewImgFile reviewImgFile2 =
        CustomFileFixture.createFile(1L, FileType.REVIEWS).toReivewFile();
    SampleFile sampleFile1 = CustomFileFixture.createFile(1L, FileType.SAMPLES).toSampleFile();
    SampleFile sampleFile2 = CustomFileFixture.createFile(1L, FileType.SAMPLES).toSampleFile();

    // Then
    assertAll(
        () -> assertThat(coverImgFile1).isEqualTo(coverImgFile2),
        () -> assertThat(detailImgFile1).isEqualTo(detailImgFile2),
        () -> assertThat(previewFile1).isEqualTo(previewFile2),
        () -> assertThat(profileImgFile1).isEqualTo(profileImgFile2),
        () -> assertThat(reviewImgFile1).isEqualTo(reviewImgFile2),
        () -> assertThat(sampleFile1).isEqualTo(sampleFile2));
  }
}
