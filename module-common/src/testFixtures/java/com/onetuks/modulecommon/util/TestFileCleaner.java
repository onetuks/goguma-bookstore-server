package com.onetuks.modulecommon.util;

import com.onetuks.modulecommon.file.FileType;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestFileCleaner extends SimpleFileVisitor<Path> {

  private static final List<Path> staticDirectoryPaths =
      Arrays.stream(FileType.values())
          .map(fileType -> getTestFilePath(fileType.getDirectoryPath()))
          .toList();

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    try {
      if (!file.getFileName().toString().contains("mock")) {
        Files.deleteIfExists(file);
      }
    } catch (DirectoryNotEmptyException e) {
      log.info("Failed to delete file: {}", file, e);
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    try {
      if (!staticDirectoryPaths.contains(dir)) {
        Files.deleteIfExists(dir);
      }
    } catch (DirectoryNotEmptyException e) {
      log.info("Failed to delete directory: {}", dir, e);
    }
    return FileVisitResult.CONTINUE;
  }

  public void deleteAllTestStatic() {
    staticDirectoryPaths.forEach(this::deleteStaticTestFilesAndDirectories);
  }

  private void deleteStaticTestFilesAndDirectories(Path startPath) {
    try {
      Files.walkFileTree(startPath, this);
    } catch (IOException e) {
      log.info("Failed to find static test files and directories.", e);
    }
  }

  public static Path getTestFilePath(String fileName) {
    return Paths.get("src/test/resources/static" + fileName);
  }
}
