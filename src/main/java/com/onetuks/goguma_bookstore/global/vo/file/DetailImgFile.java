package com.onetuks.goguma_bookstore.global.vo.file;

import java.util.Objects;
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
    DetailImgFile that = (DetailImgFile) o;
    return Objects.equals(detailImgUri, that.detailImgUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), detailImgUri);
  }
}
