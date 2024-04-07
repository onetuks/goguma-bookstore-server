package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo.ESCROW;
import static com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo.MAIL_ORDER_SALES;
import static com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo.PROFILE;

import com.onetuks.goguma_bookstore.author_debut.model.Author;

public class AuthorFixture {

  public static Author create() {
    return Author.builder()
        .member(MemberFixture.create())
        .profileImgUri(PROFILE.getFileName())
        .nickname("빠선생님")
        .introduction("유튜브 대통령")
        .escrowServiceUri(ESCROW.getFileName())
        .mailOrderSalesUri(MAIL_ORDER_SALES.getFileName())
        .build();
  }
}
