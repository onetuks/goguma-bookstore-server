package com.onetuks.goguma_bookstore.global.vo.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onetuks.goguma_bookstore.global.vo.file.provider.FilePathProvider;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CustomFile {

  private static final String BUCKET_URL = "https://test-bucket-url.com";
  private static final int MOCK_UPS_LOWER_BOUND = 4;
  private static final int MOCK_UPS_UPPER_BOUND = 10;
  private static final int PREVIEWS_COUNT = 25;

  private final String uri;
  private final MultipartFile multipartFile;

  @JsonCreator
  public CustomFile(@JsonProperty("uri") String uri) {
    this.uri = uri;
    this.multipartFile = null;
  }

  protected CustomFile() {
    this.uri = FilePathProvider.provideDefaultProfileURI();
    this.multipartFile = null;
  }

  protected CustomFile(String uri, MultipartFile multipartFile) {
    this.uri = uri;
    this.multipartFile = multipartFile;
  }

  protected CustomFile(long id, FileType fileType, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileURI(fileType, id);
    this.multipartFile = multipartFile;
  }

  protected CustomFile(long id, int index, FileType fileType, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileIndexedURI(fileType, id, index);
    this.multipartFile = multipartFile;
  }

  public static CustomFile of() {
    return new CustomFile();
  }

  public static CustomFile of(long id, FileType fileType, MultipartFile multipartFile) {
    return new CustomFile(id, fileType, multipartFile);
  }

  public static List<CustomFile> of(long id, FileType fileType, MultipartFile[] multipartFiles) {
    if (multipartFiles == null) {
      return List.of();
    }

    int fileCount = multipartFiles.length;

    if (fileType == FileType.DETAILS
        && (fileCount < MOCK_UPS_LOWER_BOUND || fileCount > MOCK_UPS_UPPER_BOUND)) {
      throw new IllegalArgumentException("목업 파일은 4개 이상 10개 이하로 등록해야 합니다.");
    } else if (fileType == FileType.PREVIEWS && fileCount < PREVIEWS_COUNT) {
      throw new IllegalArgumentException("미리보기 파일은 25개 이상 등록해야 합니다.");
    }

    return IntStream.range(0, multipartFiles.length)
        .mapToObj(index -> new CustomFile(id, index, fileType, multipartFiles[index]))
        .toList();
  }

  public boolean isNullFile() {
    return multipartFile == null;
  }

  protected String getBucketUrl() {
    return BUCKET_URL;
  }

  public ProfileImgFile toProfileImgFile() {
    return new ProfileImgFile(uri, multipartFile);
  }

  public CoverImgFile toCoverImgFile() {
    return new CoverImgFile(uri, multipartFile);
  }

  public SampleFile toSampleFile() {
    return new SampleFile(uri, multipartFile);
  }

  public DetailImgFile toDetailImgFile() {
    return new DetailImgFile(uri, multipartFile);
  }

  public PreviewFile toPreviewFile() {
    return new PreviewFile(uri, multipartFile);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomFile that = (CustomFile) o;
    return Objects.equals(uri, that.uri);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uri);
  }
}
