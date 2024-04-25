package com.onetuks.goguma_bookstore.global.vo.file;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreviewFile extends CustomFile {

  private String previewFileUri;

  public PreviewFile(String previewFileUri, MultipartFile multipartFile) {
    super(previewFileUri, multipartFile);
    this.previewFileUri = previewFileUri;
  }

  public String getPreviewUrl() {
    return getBucketUrl() + getPreviewFileUri();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    PreviewFile that = (PreviewFile) o;
    return Objects.equals(previewFileUri, that.previewFileUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), previewFileUri);
  }
}
