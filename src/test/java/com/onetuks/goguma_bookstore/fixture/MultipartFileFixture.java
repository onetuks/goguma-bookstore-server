package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.global.vo.file.FileType;
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

  private static final Logger log = LoggerFactory.getLogger(MultipartFileFixture.class);

  public static MultipartFile createFile(String fileName, FileType fileType) {
    try {
      Path path = getTempFilePath(fileName);
      if (!Files.exists(path)) {
        Files.createFile(path);
      }
      String contentType = fileType.getFileExtension();
      byte[] content = Files.readAllBytes(path);
      return new MockMultipartFile(fileName, fileName, contentType, content);
    } catch (IOException e) {
      log.info("Failed to create a mock file.", e);
      return null;
    }
  }

  public static void deleteAllStaticTestFiles() {
    Arrays.stream(FileType.values())
        .map(fileType -> getTempFilePath(fileType.getDirectoryPath()))
        .forEach(
            path -> {
              try {
                Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(filePath -> !filePath.toString().contains("mock"))
                    .forEach(
                        filePath -> {
                          try {
                            Files.delete(filePath);
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
    return Paths.get("src/test/resources/static" + fileName);
  }
}
