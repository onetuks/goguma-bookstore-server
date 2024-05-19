package com.onetuks.coredomain.global.file.filepath;

public record ProfileImgFilePath(String profileImgUri) implements CustomFilePath {

  public static ProfileImgFilePath of(String profileImgUri) {
    return new ProfileImgFilePath(profileImgUri);
  }

  @Override
  public String getUri() {
    return this.profileImgUri;
  }

  @Override
  public String getUrl() {
    return BUCKET_URL + getUri();
  }
}
