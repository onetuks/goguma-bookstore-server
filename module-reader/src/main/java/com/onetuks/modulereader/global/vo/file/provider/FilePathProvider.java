package com.onetuks.modulereader.global.vo.file.provider;

import com.onetuks.modulereader.global.vo.file.FileType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilePathProvider {

  public static String provideFileURI(FileType fileType, Long id) {
    return fileType.getDirectoryPath() + id + fileType.getFileExtension();
  }

  public static String provideFileIndexedURI(FileType fileType, long id, int index) {
    return fileType.getDirectoryPath() + id + "/" + index + fileType.getFileExtension();
  }

  public static String provideDefaultMemberProfileURI() {
    return FileType.PROFILES.getDirectoryPath() + "default-member-profile.png";
  }
}
