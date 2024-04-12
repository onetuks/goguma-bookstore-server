package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.global.vo.auth.ClientProvider;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.order.DefaultAddressInfo;
import com.onetuks.goguma_bookstore.global.vo.order.DefaultCashReceiptInfo;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.vo.AuthInfo;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

public class MemberFixture {

  public static Member create(RoleType roleType) {
    return Member.builder()
        .authInfo(createAuthInfo(roleType))
        .nickname("빡친감자" + UUIDProvider.getUUID())
        .alarmPermission(true)
        .defaultAddressInfo(
            DefaultAddressInfo.builder()
                .defaultAddress("강원도 춘천시")
                .defaultAddressDetail("어딘가")
                .build())
        .defaultCashReceiptType(
            DefaultCashReceiptInfo.builder()
                .defaultCashReceiptType(CashReceiptType.PERSON)
                .defaultCashReceiptNumber("1234-1234")
                .build())
        .build();
  }

  public static UserData createUserData(RoleType roleType) {
    return UserData.builder()
        .name("빠니보틀" + UUIDProvider.getUUID())
        .socialId(UUIDProvider.getUUID())
        .clientProvider(ClientProvider.NAVER)
        .roleType(roleType)
        .build();
  }

  private static AuthInfo createAuthInfo(RoleType roleType) {
    return AuthInfo.from(createUserData(roleType));
  }
}
