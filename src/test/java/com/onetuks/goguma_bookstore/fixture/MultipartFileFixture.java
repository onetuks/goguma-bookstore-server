package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFixture {

  private static final FileURIProviderService fileURIProviderService = new FileURIProviderService();

  public static MultipartFile createFile(FileType fileType, long id) throws IOException {
    String fileName = fileURIProviderService.provideFileURI(fileType, id);
    Path path = getTempFilePath(fileName);
    if (!Files.exists(path)) {
      Files.createFile(path);
    }
    String contentType = fileType.getFileExtension();
    byte[] content = Files.readAllBytes(path);
    return new MockMultipartFile(fileName, fileName, contentType, content);
  }

  public static void deleteFile(String fileName) throws IOException {
    Path path = getTempFilePath(fileName);
    if (Files.exists(path)) {
      Files.delete(path);
    }
  }

  private static Path getTempFilePath(String fileName) {
    return Paths.get("src/main/resources/static/" + fileName);
  }
}
