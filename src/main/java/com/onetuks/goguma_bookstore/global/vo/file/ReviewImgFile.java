package com.onetuks.goguma_bookstore.global.vo.file;

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
}
