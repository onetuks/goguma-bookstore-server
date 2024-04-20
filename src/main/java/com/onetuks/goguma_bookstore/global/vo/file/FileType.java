package com.onetuks.goguma_bookstore.global.vo.file;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import lombok.Getter;

@Getter
public enum FileType {
  ESCROWS("/escrows/", APPLICATION_PDF_VALUE),
  MAIL_ORDER_SALES("/mail-order-sales/", APPLICATION_PDF_VALUE),
  PROFILES("/profiles/", IMAGE_PNG_VALUE),
  COVERS("/covers/", IMAGE_PNG_VALUE),
  SAMPLES("/samples/", APPLICATION_PDF_VALUE),
  DETAILS("/details/", IMAGE_PNG_VALUE),
  PREVIEWS("/previews/", APPLICATION_PDF_VALUE),
  ;

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
