package com.onetuks.modulepersistence.global.vo.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImgFilePath implements CustomFilePath {

  private String reviewImgUri;

  @JsonCreator
  public ReviewImgFilePath(@JsonProperty("uri") String reviewImgUri) {
    this.reviewImgUri = reviewImgUri;
  }

  @Override
  public String getUri() {
    return this.reviewImgUri;
  }

  @Override
  @JsonIgnore
  @Transient
  public String getUrl() {
    return BUCKET_URL + getUri();
  }

  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Embeddable
  public static class ReviewImgFilePaths {

    @Type(JsonType.class)
    @Column(name = "review_img_uris")
    private List<ReviewImgFilePath> paths;

    protected ReviewImgFilePaths(List<ReviewImgFilePath> paths) {
      this.paths = paths;
    }

    public static ReviewImgFilePaths of(List<String> reviewImgUris) {
      return new ReviewImgFilePaths(reviewImgUris.stream().map(ReviewImgFilePath::new).toList());
    }

    public List<String> getUris() {
      return paths.stream().map(ReviewImgFilePath::getUri).toList();
    }

    public List<String> getUrls() {
      return paths.stream().map(ReviewImgFilePath::getUrl).toList();
    }
  }
}
