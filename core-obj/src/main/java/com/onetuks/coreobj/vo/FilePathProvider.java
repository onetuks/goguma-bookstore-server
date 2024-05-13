package com.onetuks.coreobj.vo;

import com.onetuks.coreobj.enums.file.FileType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilePathProvider {

  public static String provideFileURI(FileType fileType, String uuid) {
    return fileType.getDirectoryPath() + uuid + fileType.getFileExtension();
  }

  public static String provideFileIndexedURI(FileType fileType, String uuid, int index) {
    return fileType.getDirectoryPath() + uuid + "/" + index + fileType.getFileExtension();
  }

  public static String provideDefaultProfileURI() {
    return FileType.PROFILES.getDirectoryPath() + "default-member-profile.png";
  }
}
