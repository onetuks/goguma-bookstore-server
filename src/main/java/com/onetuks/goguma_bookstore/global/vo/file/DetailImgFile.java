package com.onetuks.goguma_bookstore.global.vo.file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailImgFile extends CustomFile {

  private String detailImgUri;

  public DetailImgFile(String detailImgUri, MultipartFile multipartFile) {
    super(detailImgUri, multipartFile);
    this.detailImgUri = detailImgUri;
  }

  public String getDetailImgUrl() {
    return getBucketUrl() + getDetailImgUri();
  }
}
