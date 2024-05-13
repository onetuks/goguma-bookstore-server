package com.onetuks.coredomain.file.filepath;

import java.util.List;

public record PreviewFilePath(
    String previewFileUri
) implements CustomFilePath {

  @Override
  public String getUri() {
    return this.previewFileUri;
  }

  @Override
  public String getUrl() {
    return BUCKET_URL + getUri();
  }

  public record PreviewFilePaths(
      List<PreviewFilePath> paths
  ) {

    public static PreviewFilePaths of(List<String> previewFileUris) {
      return new PreviewFilePaths(previewFileUris.stream().map(PreviewFilePath::new).toList());
    }

    public List<String> getUris() {
      return paths.stream().map(PreviewFilePath::getUri).toList();
    }

    public List<String> getUrls() {
      return paths.stream().map(PreviewFilePath::getUrl).toList();
    }
  }
}
