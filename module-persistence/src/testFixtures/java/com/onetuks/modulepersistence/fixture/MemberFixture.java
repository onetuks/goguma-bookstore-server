package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulecommon.util.UUIDProvider;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.order.vo.DefaultAddressInfo;
import com.onetuks.modulepersistence.order.vo.DefaultCashReceiptInfo;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.embedded.AuthInfo;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import java.util.List;

public class MemberFixture {

  public static MemberEntity create(RoleType roleType) {
    return MemberEntity.builder()
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

  private static AuthInfo createAuthInfo(RoleType roleType) {
    return AuthInfo.builder()
        .name("빠니보틀")
        .socialId(UUIDProvider.getUUID())
        .clientProvider(ClientProvider.NAVER)
        .roleTypes(List.of(roleType))
        .build();
  }
}
