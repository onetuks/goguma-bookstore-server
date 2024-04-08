package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import java.util.Random;

public class MemberFixture {

  public static Member create(RoleType roleType) {
    return Member.builder()
        .name("빠니보틀")
        .socialId(String.valueOf(new Random().longs(1, 1_024)))
        .clientProvider(ClientProvider.NAVER)
        .roleType(roleType)
        .nickname("빡친감자")
        .defaultAddress("강원도 춘천시")
        .defaultAddressDetail("어딘가")
        .defaultCashReceiptType(CashReceiptType.PERSON)
        .defaultCashReceiptNumber("1234-1234")
        .build();
  }
}
