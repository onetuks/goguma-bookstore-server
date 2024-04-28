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
public class DetailImgFilePath implements CustomFilePath {

  private String detailImgUri;

  @JsonCreator
  protected DetailImgFilePath(@JsonProperty("uri") String detailImgUri) {
    this.detailImgUri = detailImgUri;
  }

  @Override
  public String getUri() {
    return this.detailImgUri;
  }

  @Override
  @JsonIgnore
  @Transient
  public String getUrl() {
    return BUCKET_URL + getUri();
  }

  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Embeddable
  public static class DetailImgFilePaths {

    @Type(JsonType.class)
    @Column(name = "detail_img_uris", nullable = false)
    private List<DetailImgFilePath> paths;

    protected DetailImgFilePaths(List<DetailImgFilePath> paths) {
      this.paths = paths;
    }

    public static DetailImgFilePaths of(List<String> detailImgUris) {
      return new DetailImgFilePaths(
          detailImgUris.stream().map(DetailImgFilePath::new).toList());
    }

    public List<String> getUris() {
      return paths.stream()
          .map(DetailImgFilePath::getUri)
          .toList();
    }

    public List<String> getUrls() {
      return paths.stream()
          .map(DetailImgFilePath::getUrl)
          .toList();
    }
  }
}
