package com.onetuks.coredomain;

import com.onetuks.filestorage.vo.FileType;
import com.onetuks.filestorage.vo.FileWrapper;
import com.onetuks.filestorage.vo.FileWrapper.FileWrapperCollection;
import com.onetuks.filestorage.fixture.FileWrapperFixture;
import com.onetuks.filestorage.util.RandomValueProvider;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.book.entity.embedded.BookConceptualEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPhysicalInfoEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPriceInfoEmbedded;
import com.onetuks.dbstorage.book.vo.Category;
import com.onetuks.dbstorage.registration.entity.RegistrationEntity;
import com.onetuks.dbstorage.registration.entity.embedded.ApprovalInfo;
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

  private static BookConceptualEmbedded createBookConceptualInfo() {
    return BookConceptualEmbedded.builder()
        .title("아메리카 여행기")
        .oneLiner("대 빠니보틀 여행기")
        .summary("미국 갔다가 남극 직전에 돌아옴")
        .categories(List.of(Category.CARTOON, Category.ESSEY))
        .publisher("샌드박스")
        .isbn(RandomValueProvider.createIsbn())
        .build();
  }

  private static BookPhysicalInfoEmbedded createBookPhysicalInfo() {
    return BookPhysicalInfoEmbedded.builder()
        .height(200)
        .width(100)
        .coverType("양장본")
        .pageCount(500L)
        .build();
  }

  private static BookPriceInfoEmbedded createBookPriceInfo() {
    return BookPriceInfoEmbedded.builder()
        .regularPrice(10_000L)
        .purchasePrice(9_000L)
        .promotion(Boolean.TRUE)
        .stockCount(10L)
        .build();
  }
}
