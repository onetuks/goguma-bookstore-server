package com.onetuks.goguma_bookstore.global.vo.file;

import com.onetuks.goguma_bookstore.global.service.vo.File;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProfileImg extends File {

  @Column(name = "profile_img_uri", nullable = false)
  private String profileImgUri;

  public ProfileImg(String profileImgUri) {
    this.profileImgUri = profileImgUri;
  }

  public String getProfileImgUrl() {
    return getBucketUrl() + getProfileImgUri();
  }
}
