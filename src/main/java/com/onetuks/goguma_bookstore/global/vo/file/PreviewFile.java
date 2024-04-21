package com.onetuks.goguma_bookstore.global.vo.file;

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
}
