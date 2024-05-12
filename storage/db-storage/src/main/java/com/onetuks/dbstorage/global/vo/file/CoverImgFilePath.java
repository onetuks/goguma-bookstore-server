package com.onetuks.dbstorage.global.vo.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CoverImgFilePath implements CustomFilePath {

  @Column(name = "cover_img_uri", nullable = false)
  private String coverImgUri;

  @JsonCreator
  protected CoverImgFilePath(@JsonProperty("uri") String coverImgUri) {
    this.coverImgUri = coverImgUri;
  }

  public static CoverImgFilePath of(String coverImgUri) {
    return new CoverImgFilePath(coverImgUri);
  }

  @Override
  public String getUri() {
    return this.coverImgUri;
  }

  @Override
  @JsonIgnore
  @Transient
  public String getUrl() {
    return BUCKET_URL + getUri();
  }
}
