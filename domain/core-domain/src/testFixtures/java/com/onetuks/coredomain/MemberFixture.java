package com.onetuks.coredomain;

import com.onetuks.filestorage.util.UUIDProvider;
import com.onetuks.dbstorage.global.vo.auth.ClientProvider;
import com.onetuks.dbstorage.global.vo.auth.RoleType;
import com.onetuks.dbstorage.member.embedded.AuthInfo;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.order.vo.CashReceiptType;
import com.onetuks.dbstorage.order.vo.DefaultAddressInfo;
import com.onetuks.dbstorage.order.vo.DefaultCashReceiptInfo;
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
