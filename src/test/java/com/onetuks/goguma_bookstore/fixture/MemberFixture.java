package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

public class MemberFixture {

  public static Member create() {
    return Member.builder()
        .name("빠니보틀")
        .socialId("1234")
        .clientProvider(ClientProvider.NAVER)
        .roleType(RoleType.USER)
        .nickname("빡친감자")
        .defaultAddress("강원도 춘천시")
        .defaultAddressDetail("어딘가")
        .defaultCashReceiptType(CashReceiptType.PERSON)
        .defaultCashReceiptNumber("1234-1234")
        .build();
  }
}
