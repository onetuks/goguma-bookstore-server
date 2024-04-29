package com.onetuks.modulereader.fixture;

import com.onetuks.modulereader.global.vo.file.FileType;
import com.onetuks.modulereader.global.vo.file.FileWrapper;
import com.onetuks.modulereader.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulereader.global.vo.file.provider.FilePathProvider;
import java.util.stream.IntStream;
import org.springframework.web.multipart.MultipartFile;

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

    MultipartFile[] multipartFiles =
        IntStream.range(0, count)
            .mapToObj(
                index ->
                    MultipartFileFixture.create(
                        FilePathProvider.provideFileIndexedURI(fileType, id, index), fileType))
            .toArray(MultipartFile[]::new);

    return FileWrapperCollection.of(id, fileType, multipartFiles);
  }
}
