package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.util.RandomValueProvider;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.book.entity.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.entity.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.entity.embedded.BookPriceInfo;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
import com.onetuks.modulepersistence.registration.entity.embedded.ApprovalInfo;
import java.util.List;

public class RegistrationFixture {

  public static RegistrationEntity create(AuthorEntity authorEntity) {
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorEntity.getAuthorId(), FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorEntity.getAuthorId(), FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.SAMPLES);

    return RegistrationEntity.builder()
        .author(authorEntity)
        .approvalInfo(createApprovalInfo())
        .bookConceptualInfo(createBookConceptualInfo())
        .bookPhysicalInfo(createBookPhysicalInfo())
        .bookPriceInfo(createBookPriceInfo())
        .coverImgFilePath(coverImgFile.getUri())
        .detailImgFilePaths(detailImgFiles.getUris())
        .previewFilePaths(previewFiles.getUris())
        .sampleFilePath(sampleFile.getUri())
        .build();
  }

  private static ApprovalInfo createApprovalInfo() {
    return ApprovalInfo.builder()
        .approvalResult(Boolean.FALSE)
        .approvalMemo("유효하지 않은 ISBN입니다.")
        .build();
  }

  private static BookConceptualInfo createBookConceptualInfo() {
    return BookConceptualInfo.builder()
        .title("아메리카 여행기")
        .oneLiner("대 빠니보틀 여행기")
        .summary("미국 갔다가 남극 직전에 돌아옴")
        .categories(List.of(Category.CARTOON, Category.ESSEY))
        .publisher("샌드박스")
        .isbn(RandomValueProvider.createIsbn())
        .build();
  }

  private static BookPhysicalInfo createBookPhysicalInfo() {
    return BookPhysicalInfo.builder()
        .height(200)
        .width(100)
        .coverType("양장본")
        .pageCount(500L)
        .build();
  }

  private static BookPriceInfo createBookPriceInfo() {
    return BookPriceInfo.builder()
        .regularPrice(10_000L)
        .purchasePrice(9_000L)
        .promotion(Boolean.TRUE)
        .stockCount(10L)
        .build();
  }
}
