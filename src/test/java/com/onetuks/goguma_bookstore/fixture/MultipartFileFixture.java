package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.service.vo.FileType.BOOK_COVERS;
import static com.onetuks.goguma_bookstore.global.service.vo.FileType.BOOK_SAMPLES;
import static com.onetuks.goguma_bookstore.global.service.vo.FileType.ESCROWS;
import static com.onetuks.goguma_bookstore.global.service.vo.FileType.PROFILES;

import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFixture {

  public static MultipartFile createFile(FileType fileType) throws IOException {
    MockMultipartFileInfo info = MockMultipartFileInfo.getMockMultipartFileInfo(fileType);
    String fileName = info.getFileName();
    Path path =
        new ClassPathResource("static/" + info.getPackageName() + fileName).getFile().toPath();
    String contentType = info.getFileType().getFileExtension();
    byte[] content = Files.readAllBytes(path);
    return new MockMultipartFile(fileName, fileName, contentType, content);
  }

  public enum MockMultipartFileInfo {
    ESCROW("escrow-sample.pdf", "escrows/", ESCROWS),
    MAIL_ORDER_SALES("mos-sample.pdf", "mail-order-sales/", FileType.MAIL_ORDER_SALES),
    PROFILE("profile-sample.png", "profiles/", PROFILES),
    BOOK_COVER("book-cover-sample.png", "book-covers/", BOOK_COVERS),
    BOOK_SAMPLE("book-sample-sample.pdf", "book-samples/", BOOK_SAMPLES);

    private final String fileName;
    private final String packageName;
    private final FileType fileType;

    MockMultipartFileInfo(String fileName, String packageName, FileType fileType) {
      this.fileName = fileName;
      this.packageName = packageName;
      this.fileType = fileType;
    }

    public static MockMultipartFileInfo getMockMultipartFileInfo(FileType fileType) {
      return Arrays.stream(MockMultipartFileInfo.values())
          .filter(mockMultipartFileInfo -> mockMultipartFileInfo.getFileType() == fileType)
          .findAny()
          .orElse(PROFILE);
    }

    public String getFileName() {
      return fileName;
    }

    public String getPackageName() {
      return packageName;
    }

    public FileType getFileType() {
      return fileType;
    }
  }
}
