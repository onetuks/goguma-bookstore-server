package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.book.model.vo.Category.CARTOON;
import static com.onetuks.goguma_bookstore.book.model.vo.Category.ESSEY;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.COVERS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.DETAILS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.PREVIEWS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.SAMPLES;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createIsbn;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.vo.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPriceInfo;
import com.onetuks.goguma_bookstore.global.vo.file.CoverImgFile;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.DetailImgFile;
import com.onetuks.goguma_bookstore.global.vo.file.PreviewFile;
import com.onetuks.goguma_bookstore.global.vo.file.SampleFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.model.vo.ApprovalInfo;
import java.util.List;

public class RegistrationFixture {

  public static Registration create(Author author) {
    CoverImgFile coverImgFile =
        CustomFileFixture.createFile(author.getAuthorId(), COVERS).toCoverImgFile();
    List<DetailImgFile> detailImgFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), DETAILS).stream()
            .map(CustomFile::toDetailImgFile)
            .toList();
    List<PreviewFile> previewFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), PREVIEWS).stream()
            .map(CustomFile::toPreviewFile)
            .toList();
    SampleFile sampleFile =
        CustomFileFixture.createFile(author.getAuthorId(), SAMPLES).toSampleFile();

    return Registration.builder()
        .author(author)
        .approvalInfo(createApprovalInfo())
        .bookConceptualInfo(createBookConceptualInfo())
        .bookPhysicalInfo(createBookPhysicalInfo())
        .bookPriceInfo(createBookPriceInfo())
        .publisher("샌드박스")
        .stockCount(10L)
        .coverImgFile(coverImgFile)
        .detailImgFiles(detailImgFiles)
        .previewFiles(previewFiles)
        .sampleFile(sampleFile)
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
        .categories(List.of(CARTOON, ESSEY))
        .isbn(createIsbn())
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
        .build();
  }
}
