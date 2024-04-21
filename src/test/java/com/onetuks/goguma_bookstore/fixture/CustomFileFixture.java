package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.provider.FilePathProvider;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.web.multipart.MultipartFile;

public class CustomFileFixture {

  public static CustomFile createFile(long id, FileType fileType) {
    return CustomFile.of(
        id,
        fileType,
        MultipartFileFixture.create(FilePathProvider.provideFileURI(fileType, id), fileType));
  }

  public static CustomFile createNullFile() {
    return CustomFile.of();
  }

  public static List<CustomFile> createFiles(long id, FileType fileType) {
    int count = fileType == FileType.DETAILS ? 10 : 25;

    MultipartFile[] multipartFiles =
        IntStream.range(0, count)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(MultipartFile[]::new);

    return CustomFile.of(id, fileType, multipartFiles);
  }
}
