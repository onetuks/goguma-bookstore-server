package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.Category;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo;

public class BookFixture {

  public static Book create(Author author) {
    return Book.builder()
        .author(author)
        .authorNickname(author.getNickname())
        .coverImgUri(MockMultipartFileInfo.BOOK_COVER.getFileName())
        .title("유라시아 여행기")
        .category(Category.ESSEY)
        .summary("대충 베트남에서 시작해서 유럽에서 끝남")
        .price(10_000L)
        .stockCount(10L)
        .isbn("1234GH1234")
        .publisher("샌드박스")
        .promotion(Boolean.TRUE)
        .build();
  }
}
