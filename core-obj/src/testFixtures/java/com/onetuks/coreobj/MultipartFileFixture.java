package com.onetuks.coreobj;

import com.onetuks.coreobj.enums.file.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFixture {

  private static final Logger log = LoggerFactory.getLogger(MultipartFileFixture.class);

  public static MultipartFile create(String fileName, FileType fileType) {
    try {
      Path path = getTestFilePath(fileName);
      Files.createDirectories(path.getParent());
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

  public static Path getTestFilePath(String fileName) {
    return Paths.get("src/test/resources/static" + fileName);
  }
}
