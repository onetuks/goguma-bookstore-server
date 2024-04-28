package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.util.UUIDProvider;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.global.vo.order.DefaultAddressInfo;
import com.onetuks.modulepersistence.global.vo.order.DefaultCashReceiptInfo;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import com.onetuks.modulepersistence.member.vo.UserData;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;

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
        .defaultCashReceiptInfo(
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
