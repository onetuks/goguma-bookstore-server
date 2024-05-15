package com.onetuks.coredomain.file.filepath;

import java.util.List;

public record ReviewImgFilePath(String reviewImgUri) implements CustomFilePath {

  @Override
  public String getUri() {
    return this.reviewImgUri;
  }

  @Override
  public String getUrl() {
    return BUCKET_URL + getUri();
  }

  public record ReviewImgFilePaths(List<ReviewImgFilePath> paths) {

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
