package com.onetuks.goguma_bookstore.global.vo.file.provider;

import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilePathProvider {

  public static String provideFileURI(FileType fileType, Long id) {
    return fileType.getDirectoryPath() + id + fileType.getFileExtension();
  }

  public static String provideFileIndexedURI(FileType fileType, long id, int index) {
    return fileType.getDirectoryPath() + id + "_" + index + fileType.getFileExtension();
  }

  public static String provideDefaultProfileURI() {
    return FileType.PROFILES.getDirectoryPath() + "default-profile.png";
  }
}
