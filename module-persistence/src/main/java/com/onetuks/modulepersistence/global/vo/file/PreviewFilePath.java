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
public class PreviewFilePath implements CustomFilePath {

  private String previewFileUri;

  @JsonCreator
  protected PreviewFilePath(@JsonProperty("uri") String previewFileUri) {
    this.previewFileUri = previewFileUri;
  }

  @Override
  public String getUri() {
    return this.previewFileUri;
  }

  @Override
  @JsonIgnore
  @Transient
  public String getUrl() {
    return BUCKET_URL + getUri();
  }

  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Embeddable
  public static class PreviewFilePaths {

    @Type(JsonType.class)
    @Column(name = "preview_uris", nullable = false)
    private List<PreviewFilePath> paths;

    protected PreviewFilePaths(List<PreviewFilePath> paths) {
      this.paths = paths;
    }

    public static PreviewFilePaths of(List<String> previewFileUris) {
      return new PreviewFilePaths(
          previewFileUris.stream().map(PreviewFilePath::new).toList());
    }

    public List<String> getUris() {
      return paths.stream().map(PreviewFilePath::getUri).toList();
    }

    public List<String> getUrls() {
      return paths.stream().map(PreviewFilePath::getUrl).toList();
    }
  }
}
