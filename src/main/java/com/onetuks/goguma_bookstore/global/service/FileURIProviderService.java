package com.onetuks.goguma_bookstore.global.service;

import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import org.springframework.stereotype.Component;

@Component
public class FileURIProviderService {

  public String provideFileURI(FileType fileType, Long id) {
    return fileType.getDirectoryPath() + id + fileType.getFileExtension();
  }

  public String provideDefaultProfileURI() {
    return FileType.PROFILES.getDirectoryPath() + "default-profile.png";
  }
}
