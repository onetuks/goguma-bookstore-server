package com.onetuks.goguma_bookstore.global.service;

import org.springframework.stereotype.Component;

@Component
public class FileURIProvider {

  public String provideFileURI(FileType fileType, Long id) {
    return fileType.getDirectoryPath() + String.valueOf(id) + fileType.getFileExtension();
  }
}
