package com.onetuks.coredomain.file.filepath;

public record SampleFilePath(String sampleFileUri) implements CustomFilePath {

  public static SampleFilePath of(String sampleFileUri) {
    return new SampleFilePath(sampleFileUri);
  }

  @Override
  public String getUri() {
    return this.sampleFileUri;
  }

  @Override
  public String getUrl() {
    return BUCKET_URL + getUri();
  }
}
