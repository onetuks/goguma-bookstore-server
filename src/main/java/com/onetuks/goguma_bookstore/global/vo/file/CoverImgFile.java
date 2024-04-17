package com.onetuks.goguma_bookstore.global.vo.file;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CoverImgFile extends CustomFile {

  @Column(name = "cover_img_uri", nullable = false)
  private String coverImgUri;

  public CoverImgFile(String coverImgUri, MultipartFile multipartFile) {
    super(coverImgUri, multipartFile);
    this.coverImgUri = coverImgUri;
  }

  public String getCoverImgUrl() {
    return getBucketUrl() + getCoverImgUri();
  }
}