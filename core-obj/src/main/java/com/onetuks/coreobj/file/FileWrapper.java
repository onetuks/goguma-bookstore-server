package com.onetuks.coreobj.file;

import com.onetuks.coreobj.annotation.Generated;
import com.onetuks.coreobj.enums.file.FileType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FileWrapper {

  private static final int DETAILS_LOWER_BOUND = 4;
  private static final int DETAILS_UPPER_BOUND = 10;
  private static final int PREVIEWS_COUNT = 25;
  private static final int REVIEWS_COUNT = 5;

  private final String uri;
  private final MultipartFile multipartFile;

  private FileWrapper() {
    this.uri = FilePathProvider.provideDefaultProfileURI();
    this.multipartFile = null;
  }

  private FileWrapper(FileType fileType, String uuid, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileURI(fileType, uuid);
    this.multipartFile = multipartFile;
  }

  private FileWrapper(FileType fileType, String uuid, int index, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileIndexedURI(fileType, uuid, index);
    this.multipartFile = multipartFile;
  }

  public static FileWrapper of() {
    return new FileWrapper();
  }

  public static FileWrapper of(FileType fileType, String uuid, MultipartFile multipartFile) {
    return new FileWrapper(fileType, uuid, multipartFile);
  }

  public boolean isNullFile() {
    return multipartFile == null;
  }

  public record FileWrapperCollection(List<FileWrapper> fileWrappers) {

    public static FileWrapperCollection of(
        FileType fileType, String uuid, MultipartFile[] multipartFiles) {
      if (multipartFiles == null) {
        return new FileWrapperCollection(Collections.emptyList());
      }

      int fileCount = multipartFiles.length;

      if (fileType == FileType.DETAILS
          && (fileCount < DETAILS_LOWER_BOUND || fileCount > DETAILS_UPPER_BOUND)) {
        throw new IllegalArgumentException("상세 이미지 파일은 4개 이상 10개 이하로 등록해야 합니다.");
      } else if (fileType == FileType.PREVIEWS && fileCount < PREVIEWS_COUNT) {
        throw new IllegalArgumentException("미리보기 파일은 25개 이상 등록해야 합니다.");
      } else if (fileType == FileType.REVIEWS && fileCount > REVIEWS_COUNT) {
        throw new IllegalArgumentException("리뷰 이미지 파일은 5개 이하로 등록해야 합니다.");
      }

      return new FileWrapperCollection(
          IntStream.range(0, multipartFiles.length)
              .mapToObj(index -> new FileWrapper(fileType, uuid, index, multipartFiles[index]))
              .toList());
    }

    public List<String> getUris() {
      return this.fileWrappers.stream().map(FileWrapper::getUri).toList();
    }

    public boolean isEmpty() {
      return fileWrappers.isEmpty();
    }
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileWrapper that = (FileWrapper) o;
    return Objects.equals(uri, that.uri);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(uri);
  }
}
