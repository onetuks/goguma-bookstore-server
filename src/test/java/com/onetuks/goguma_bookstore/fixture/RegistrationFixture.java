package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.BOOK_COVERS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.BOOK_SAMPLES;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import java.io.IOException;

public class RegistrationFixture {

  public static Registration create(Author author) throws IOException {
    return Registration.builder()
        .author(author)
        .approvalResult(Boolean.FALSE)
        .approvalMemo("통판신고증 누락함 다시 해주세요.")
        .coverImgUri(CustomFileFixture.create(author.getAuthorId(), BOOK_COVERS).getUri())
        .title("아메리카 여행기")
        .summary("미국 갔다가 남극 직전에 돌아옴")
        .price(10_000L)
        .stockCount(10L)
        .isbn("1234GH1234")
        .publisher("샌드박스")
        .promotion(Boolean.TRUE)
        .sampleUri(CustomFileFixture.create(author.getAuthorId(), BOOK_SAMPLES).getUri())
        .build();
  }
}
