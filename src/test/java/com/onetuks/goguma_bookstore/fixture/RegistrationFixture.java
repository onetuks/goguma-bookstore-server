package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.BOOK_COVERS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.BOOK_SAMPLES;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.registration.model.Registration;

public class RegistrationFixture {

  public static Registration create(Author author) {
    return Registration.builder()
        .author(author)
        .approvalResult(Boolean.FALSE)
        .approvalMemo("유효하지 않은 ISBN입니다.")
        .coverImgFile(CustomFileFixture.create(author.getAuthorId(), BOOK_COVERS).toCoverImgFile())
        .title("아메리카 여행기")
        .summary("미국 갔다가 남극 직전에 돌아옴")
        .price(10_000L)
        .stockCount(10L)
        .isbn("1234GH1234")
        .publisher("샌드박스")
        .promotion(Boolean.TRUE)
        .sampleFile(CustomFileFixture.create(author.getAuthorId(), BOOK_SAMPLES).toSampleFile())
        .build();
  }
}
