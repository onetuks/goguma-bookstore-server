package com.onetuks.coreobj;

import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FilePathProvider;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import java.util.stream.IntStream;

public class FileWrapperFixture {

  public static FileWrapper createFile(FileType fileType, String uuid) {
    return FileWrapper.of(
        fileType,
        uuid,
        MultipartFileFixture.create(FilePathProvider.provideFileURI(fileType, uuid), fileType));
  }

  public static FileWrapper createNullFile() {
    return FileWrapper.of();
  }

  public static FileWrapperCollection createFiles(FileType fileType, String uuid) {
    int count = fileType == FileType.PREVIEWS ? 25 : 10;

    org.springframework.web.multipart.MultipartFile[] multipartFiles =
        IntStream.range(0, count)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, uuid, index), fileType))
            .toArray(org.springframework.web.multipart.MultipartFile[]::new);

    return FileWrapperCollection.of(fileType, uuid, multipartFiles);
  }
}
