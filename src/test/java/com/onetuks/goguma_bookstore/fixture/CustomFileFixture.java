package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.provider.FilePathProvider;

public class CustomFileFixture {

  public static CustomFile create(long id, FileType fileType) {
    return CustomFile.of(
        id,
        fileType,
        MultipartFileFixture.createFile(FilePathProvider.provideFileURI(fileType, id), fileType));
  }

  public static CustomFile createNullFile() {
    return CustomFile.of();
  }
}
