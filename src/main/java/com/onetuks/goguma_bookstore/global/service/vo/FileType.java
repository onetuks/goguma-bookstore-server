package com.onetuks.goguma_bookstore.global.service.vo;

import lombok.Getter;

@Getter
public enum FileType {
  ESCROWS("escrows/", ".pdf"),
  MAIL_ORDER_SALES("mail-order-sales/", ".pdf"),
  PROFILES("profiles/", ".png"),
  BOOK_COVERS("book-covers/", ".png"),
  BOOK_SAMPLES("book-samples/", ".pdf");

  private final String directoryPath;
  private final String fileExtension;

  FileType(String directoryPath, String fileExtension) {
    this.directoryPath = directoryPath;
    this.fileExtension = fileExtension;
  }
}
