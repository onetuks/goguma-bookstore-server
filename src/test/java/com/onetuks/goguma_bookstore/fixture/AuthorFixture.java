package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo.ESCROW;
import static com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo.MAIL_ORDER_SALES;
import static com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo.PROFILE;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;

public class AuthorFixture {

  public static Author create(Member member) {
    return Author.builder()
        .member(member)
        .profileImgUri(PROFILE.getFileName())
        .nickname("빠선생님")
        .introduction("유튜브 대통령")
        .escrowServiceUri(ESCROW.getFileName())
        .mailOrderSalesUri(MAIL_ORDER_SALES.getFileName())
        .enrollPassed(false)
        .build();
  }

  public static AuthorCreateParam createCreationParam() {
    return new AuthorCreateParam("빠선생님", "유튜브 대통령");
  }
}
