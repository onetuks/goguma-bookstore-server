package com.onetuks.modulereader.fixture;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.util.RandomValueProvider;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.book.model.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPriceInfo;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.registration.model.Registration;
import com.onetuks.modulepersistence.registration.model.embedded.ApprovalInfo;
import java.util.List;

public class RegistrationFixture {

  public static Registration create(Author author) {
    FileWrapper coverImgFile = FileWrapperFixture.createFile(author.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    return Registration.builder()
        .author(author)
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
