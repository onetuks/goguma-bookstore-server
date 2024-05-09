package com.onetuks.modulepersistence.global.vo.file;

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
public class SampleFilePath implements CustomFilePath {

  @Column(name = "sample_uri", nullable = false)
  private String sampleUri;

  @JsonCreator
  protected SampleFilePath(@JsonProperty("uri") String sampleUri) {
    this.sampleUri = sampleUri;
  }

  public static SampleFilePath of(String sampleUri) {
    return new SampleFilePath(sampleUri);
  }

  @Override
  public String getUri() {
    return this.sampleUri;
  }

  @Override
  @JsonIgnore
  @Transient
  public String getUrl() {
    return BUCKET_URL + getUri();
  }
}
