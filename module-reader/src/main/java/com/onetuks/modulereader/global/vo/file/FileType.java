package com.onetuks.modulereader.global.vo.file;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import lombok.Getter;

@Getter
public enum FileType {
  PROFILES("/profiles/", IMAGE_PNG_VALUE),
  COVERS("/covers/", IMAGE_PNG_VALUE),
  SAMPLES("/samples/", APPLICATION_PDF_VALUE),
  DETAILS("/details/", IMAGE_PNG_VALUE),
  PREVIEWS("/previews/", APPLICATION_PDF_VALUE),
  REVIEWS("/reviews/", IMAGE_PNG_VALUE);

  private final String directoryPath;
  private final String mediaType;

  FileType(String directoryPath, String mediaType) {
    this.directoryPath = directoryPath;
    this.mediaType = mediaType;
  }

  public String getFileExtension() {
    return "." + this.mediaType.split("/")[1];
  }
}
