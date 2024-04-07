package com.onetuks.goguma_bookstore.global.service.vo;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import lombok.Getter;

@Getter
public enum FileType {
  ESCROWS("escrows/", APPLICATION_PDF_VALUE),
  MAIL_ORDER_SALES("mail-order-sales/", APPLICATION_PDF_VALUE),
  PROFILES("profiles/", IMAGE_PNG_VALUE),
  BOOK_COVERS("book-covers/", IMAGE_PNG_VALUE),
  BOOK_SAMPLES("book-samples/", APPLICATION_PDF_VALUE);

  private final String directoryPath;
  private final String fileExtension;

  FileType(String directoryPath, String fileExtension) {
    this.directoryPath = directoryPath;
    this.fileExtension = fileExtension;
  }
}
