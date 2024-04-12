package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import com.onetuks.goguma_bookstore.member.vo.RoleType;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import java.util.Random;

public class MemberFixture {

  public static Member createUserData(RoleType roleType) {
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

  public static UserData createUserData() {
    return UserData.builder()
        .name("빠니보틀")
        .socialId(String.valueOf(new Random().longs(1, 1_024)))
        .clientProvider(ClientProvider.NAVER)
        .roleType(RoleType.USER)
        .build();
  }
}
