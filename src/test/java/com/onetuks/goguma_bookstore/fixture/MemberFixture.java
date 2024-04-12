package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.vo.AuthInfo;
import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import com.onetuks.goguma_bookstore.member.vo.RoleType;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import java.util.Random;

public class MemberFixture {

  public static Member create(RoleType roleType) {
    return Member.builder()
        .authInfo(createAuthInfo(roleType))
        .nickname("빡친감자")
        .defaultAddress("강원도 춘천시")
        .defaultAddressDetail("어딘가")
        .defaultCashReceiptType(CashReceiptType.PERSON)
        .defaultCashReceiptNumber("1234-1234")
        .alarmPermission(true)
        .build();
  }

  public static UserData createUserData(RoleType roleType) {
    return UserData.builder()
        .name("빠니보틀")
        .socialId(String.valueOf(new Random().longs(1, 1_024)))
        .clientProvider(ClientProvider.NAVER)
        .roleType(roleType)
        .build();
  }

  private static AuthInfo createAuthInfo(RoleType roleType) {
    return AuthInfo.from(createUserData(roleType));
  }
}
