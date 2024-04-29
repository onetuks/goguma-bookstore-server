package com.onetuks.modulereader.global.vo.file;

import com.onetuks.modulereader.global.vo.file.provider.FilePathProvider;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FileWrapper {

  private static final int MOCK_UPS_LOWER_BOUND = 4;
  private static final int MOCK_UPS_UPPER_BOUND = 10;
  private static final int PREVIEWS_COUNT = 25;

  private final String uri;
  private final MultipartFile multipartFile;

  private FileWrapper() {
    this.uri = FilePathProvider.provideDefaultMemberProfileURI();
    this.multipartFile = null;
  }

  private FileWrapper(long id, FileType fileType, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileURI(fileType, id);
    this.multipartFile = multipartFile;
  }

  private FileWrapper(long id, int index, FileType fileType, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileIndexedURI(fileType, id, index);
    this.multipartFile = multipartFile;
  }

  public static FileWrapper of() {
    return new FileWrapper();
  }

  public static FileWrapper of(long id, FileType fileType, MultipartFile multipartFile) {
    return new FileWrapper(id, fileType, multipartFile);
  }

  public boolean isNullFile() {
    return multipartFile == null;
  }

  @Override
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
  public int hashCode() {
    return Objects.hashCode(uri);
  }

  public record FileWrapperCollection(List<FileWrapper> fileWrappers) {

    public static FileWrapperCollection of(
        long id, FileType fileType, MultipartFile[] multipartFiles) {
      if (multipartFiles == null) {
        return new FileWrapperCollection(Collections.emptyList());
      }

      int fileCount = multipartFiles.length;

      if (fileType == FileType.DETAILS
          && (fileCount < MOCK_UPS_LOWER_BOUND || fileCount > MOCK_UPS_UPPER_BOUND)) {
        throw new IllegalArgumentException("목업 파일은 4개 이상 10개 이하로 등록해야 합니다.");
      } else if (fileType == FileType.PREVIEWS && fileCount < PREVIEWS_COUNT) {
        throw new IllegalArgumentException("미리보기 파일은 25개 이상 등록해야 합니다.");
      }

      return new FileWrapperCollection(
          IntStream.range(0, multipartFiles.length)
              .mapToObj(index -> new FileWrapper(id, index, fileType, multipartFiles[index]))
              .toList());
    }

    public List<String> getUris() {
      return this.fileWrappers.stream().map(FileWrapper::getUri).toList();
    }

    public boolean isEmpty() {
      return fileWrappers.isEmpty();
    }
  }
}
