package com.onetuks.goguma_bookstore.global.vo.file;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImgFile extends CustomFile {

  private String reviewImgUri;

  public ReviewImgFile(String reviewImgUri, MultipartFile multipartFile) {
    super(reviewImgUri, multipartFile);
    this.reviewImgUri = reviewImgUri;
  }

  public String getDetailImgUrl() {
    return getBucketUrl() + getReviewImgUri();
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
    ReviewImgFile that = (ReviewImgFile) o;
    return Objects.equals(reviewImgUri, that.reviewImgUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), reviewImgUri);
  }
}
