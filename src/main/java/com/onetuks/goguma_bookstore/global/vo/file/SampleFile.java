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
public class SampleFile extends CustomFile {

  @Column(name = "sample_uri", nullable = false)
  private String sampleUri;

  public SampleFile(String sampleUri, MultipartFile multipartFile) {
    super(sampleUri, multipartFile);
    this.sampleUri = sampleUri;
  }

  public String getSampleUrl() {
    return getBucketUrl() + getSampleUri();
  }
}
