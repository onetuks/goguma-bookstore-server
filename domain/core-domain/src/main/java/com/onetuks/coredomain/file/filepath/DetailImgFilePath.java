package com.onetuks.coredomain.file.filepath;

import java.util.List;

public record DetailImgFilePath(
    String detailImgUri
) implements CustomFilePath {

  @Override
  public String getUri() {
    return this.detailImgUri;
  }

  @Override
  public String getUrl() {
    return BUCKET_URL + getUri();
  }

  public record DetailImgFilePaths(
      List<DetailImgFilePath> paths
  ) {

    public static DetailImgFilePaths of(List<String> detailImgUris) {
      return new DetailImgFilePaths(detailImgUris.stream().map(DetailImgFilePath::new).toList());
    }

    public List<String> getUris() {
      return paths.stream().map(DetailImgFilePath::getUri).toList();
    }

    public List<String> getUrls() {
      return paths.stream().map(DetailImgFilePath::getUrl).toList();
    }
  }
}
