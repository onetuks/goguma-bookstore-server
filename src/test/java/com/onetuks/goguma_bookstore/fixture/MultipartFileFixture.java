package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFixture {

  private static final FileURIProviderService fileURIProviderService = new FileURIProviderService();
  private static final Logger log = LoggerFactory.getLogger(MultipartFileFixture.class);

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

  public static void deleteAllStaticTestFiles() {
    Arrays.stream(FileType.values())
        .map(fileType -> getTempFilePath(fileType.getDirectoryPath()))
        .forEach(
            path -> {
              try {
                Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(
                        path1 -> {
                          try {
                            Files.delete(path1);
                          } catch (IOException e) {
                            log.info("Failed to delete static test files.", e);
                          }
                        });
              } catch (IOException e) {
                log.info("Failed to find static test directory.", e);
              }
            });
  }

  private static Path getTempFilePath(String fileName) {
    return Paths.get("src/main/resources/static" + fileName);
  }
}
