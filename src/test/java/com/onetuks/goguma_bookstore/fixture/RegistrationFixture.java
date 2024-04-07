package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.author_debut.model.Author;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo;
import com.onetuks.goguma_bookstore.registration.model.Registration;

public class RegistrationFixture {

  public static Registration create(Author author) {
    return Registration.builder()
        .author(author)
        .approvalResult(Boolean.FALSE)
        .approvalMemo("통판신고증 누락함 다시 해주세요.")
        .coverImgUri(MockMultipartFileInfo.BOOK_COVER.getFileName())
        .title("아메리카 여행기")
        .summary("미국 갔다가 남극 직전에 돌아옴")
        .price(10_000L)
        .stockCount(10L)
        .isbn("1234GH1234")
        .publisher("샌드박스")
        .promotion(Boolean.TRUE)
        .sampleUri(MockMultipartFileInfo.BOOK_SAMPLE.getFileName())
        .build();
  }
}
