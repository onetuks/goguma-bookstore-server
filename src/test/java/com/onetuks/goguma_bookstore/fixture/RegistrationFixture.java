package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.book.model.vo.Category.CARTOON;
import static com.onetuks.goguma_bookstore.book.model.vo.Category.ESSEY;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.COVERS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.DETAILS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.PREVIEWS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.SAMPLES;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.model.vo.ApprovalInfo;
import java.util.List;

public class RegistrationFixture {

  public static Registration create(Author author) {
    return Registration.builder()
        .author(author)
        .approvalInfo(
            ApprovalInfo.builder()
                .approvalResult(Boolean.FALSE)
                .approvalMemo("유효하지 않은 ISBN입니다.")
                .build())
        .title("아메리카 여행기")
        .oneLiner("대 빠니보틀 여행기")
        .summary("미국 갔다가 남극 직전에 돌아옴")
        .categories(List.of(CARTOON, ESSEY))
        .publisher("샌드박스")
        .isbn("1234GH1234")
        .pageSizeInfo(new PageSizeInfo(200, 100))
        .coverType("양장본")
        .pageCount(500L)
        .price(10_000L)
        .stockCount(10L)
        .promotion(Boolean.TRUE)
        .coverImgFile(CustomFileFixture.createFile(author.getAuthorId(), COVERS).toCoverImgFile())
        .detailImgFiles(
            CustomFileFixture.createFiles(author.getAuthorId(), DETAILS).stream()
                .map(CustomFile::toMockUpFile)
                .toList())
        .previewFiles(
            CustomFileFixture.createFiles(author.getAuthorId(), PREVIEWS).stream()
                .map(CustomFile::toPreviewFile)
                .toList())
        .sampleFile(CustomFileFixture.createFile(author.getAuthorId(), SAMPLES).toSampleFile())
        .build();
  }
}
