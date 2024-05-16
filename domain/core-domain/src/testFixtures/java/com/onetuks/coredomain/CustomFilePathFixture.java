package com.onetuks.coredomain;

import com.onetuks.coredomain.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.file.filepath.ReviewImgFilePath.ReviewImgFilePaths;
import com.onetuks.coredomain.file.filepath.SampleFilePath;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FilePathProvider;
import java.util.stream.IntStream;

public class CustomFilePathFixture {

  public static CoverImgFilePath createCoverImgFilePath(String uuid) {
    return CoverImgFilePath.of(FilePathProvider.provideFileURI(FileType.COVERS, uuid));
  }

  public static DetailImgFilePaths createDetailImgFilePaths(String uuid) {
    return DetailImgFilePaths.of(
        IntStream.range(0, 5)
            .mapToObj(
                index -> FilePathProvider.provideFileIndexedURI(FileType.DETAILS, uuid, index))
            .toList());
  }

  public static PreviewFilePaths createPreviewFilePaths(String uuid) {
    return PreviewFilePaths.of(
        IntStream.range(0, 25)
            .mapToObj(
                index -> FilePathProvider.provideFileIndexedURI(FileType.PREVIEWS, uuid, index))
            .toList());
  }

  public static ProfileImgFilePath createProfileImgFilePath(String uuid) {
    return new ProfileImgFilePath(FilePathProvider.provideFileURI(FileType.PROFILES, uuid));
  }

  public static ReviewImgFilePaths createReviewImgFilePaths(String uuid) {
    return ReviewImgFilePaths.of(
        IntStream.range(0, 5)
            .mapToObj(
                index -> FilePathProvider.provideFileIndexedURI(FileType.REVIEWS, uuid, index))
            .toList());
  }

  public static SampleFilePath createSampleFilePath(String uuid) {
    return SampleFilePath.of(FilePathProvider.provideFileURI(FileType.SAMPLES, uuid));
  }
}
