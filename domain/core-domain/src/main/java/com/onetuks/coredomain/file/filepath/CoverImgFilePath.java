package com.onetuks.coredomain.file.filepath;

public record CoverImgFilePath(String coverImgUri) implements CustomFilePath {

  public static CoverImgFilePath of(String coverImgFileUri) {
    return new CoverImgFilePath(coverImgFileUri);
  }

  @Override
  public String getUri() {
    return this.coverImgUri;
  }

  @Override
  public String getUrl() {
    return BUCKET_URL + getUri();
  }
}
