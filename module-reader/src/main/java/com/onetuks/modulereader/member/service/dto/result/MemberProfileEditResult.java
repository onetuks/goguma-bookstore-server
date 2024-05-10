package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberProfileEditResult(
    long memberId,
    String nickname,
    String profileImgUrl,
    boolean alarmPermission,
    String defaultAddress,
    String defaultAddressDetail,
    CashReceiptType defaultCashReceiptType,
    String defaultCashReceiptNumber) {

  public static MemberProfileEditResult from(MemberEntity memberEntity) {
    return new MemberProfileEditResult(
        memberEntity.getMemberId(),
        memberEntity.getNickname(),
        memberEntity.getProfileImgUrl(),
        memberEntity.getAlarmPermission(),
        memberEntity.getDefaultAddress(),
        memberEntity.getDefaultAddressDetail(),
        memberEntity.getDefaultCashReceiptType(),
        memberEntity.getDefaultCashReceiptNumber());
  }
}
