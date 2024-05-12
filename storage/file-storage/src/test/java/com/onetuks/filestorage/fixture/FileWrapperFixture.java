package com.onetuks.filestorage.fixture;

import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.vo.FilePathProvider;
import com.onetuks.coreobj.vo.FileWrapper;
import com.onetuks.coreobj.vo.FileWrapper.FileWrapperCollection;
import java.util.stream.IntStream;

public class FileWrapperFixture {

  public static FileWrapper createFile(long id, FileType fileType) {
    return FileWrapper.of(
        id,
        fileType,
        MultipartFileFixture.create(FilePathProvider.provideFileURI(fileType, id), fileType));
  }

  public static FileWrapper createNullFile() {
    return FileWrapper.of();
  }

  public static FileWrapperCollection createFiles(long id, FileType fileType) {
    int count = fileType == FileType.PREVIEWS ? 25 : 10;

    org.springframework.web.multipart.MultipartFile[] multipartFiles =
        IntStream.range(0, count)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(org.springframework.web.multipart.MultipartFile[]::new);

    return FileWrapperCollection.of(id, fileType, multipartFiles);
  }
}
