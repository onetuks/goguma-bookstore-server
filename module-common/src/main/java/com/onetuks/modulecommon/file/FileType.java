package com.onetuks.modulecommon.file;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public enum FileType {
  PROFILES("/profiles/", MediaType.IMAGE_PNG_VALUE),
  COVERS("/covers/", MediaType.IMAGE_PNG_VALUE),
  SAMPLES("/samples/", MediaType.APPLICATION_PDF_VALUE),
  DETAILS("/details/", MediaType.IMAGE_PNG_VALUE),
  PREVIEWS("/previews/", MediaType.APPLICATION_PDF_VALUE),
  REVIEWS("/reviews/", MediaType.IMAGE_PNG_VALUE);

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
