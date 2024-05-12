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
public class ProfileImgFilePath implements CustomFilePath {

  @Column(name = "profile_img_uri", nullable = false)
  private String profileImgUri;

  @JsonCreator
  public ProfileImgFilePath(@JsonProperty("uri") String profileImgUri) {
    this.profileImgUri = profileImgUri;
  }

  @Override
  public String getUri() {
    return this.profileImgUri;
  }

  @Override
  @JsonIgnore
  @Transient
  public String getUrl() {
    return BUCKET_URL + getUri();
  }
}
