package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberInfoResult(
    long memberId,
    String nickname,
    String profileImgUrl,
    boolean alarmPermission,
    String defaultAddress,
    String defaultAddressDetail,
    CashReceiptType defaultCashReceiptType,
    String defaultCashReceiptNumber) {

  public static MemberInfoResult from(Member member) {
    return new MemberInfoResult(
        member.getMemberId(),
        member.getNickname(),
        member.getProfileImgUrl(),
        member.getAlarmPermission(),
        member.getDefaultAddress(),
        member.getDefaultAddressDetail(),
        member.getDefaultCashReceiptType(),
        member.getDefaultCashReceiptNumber());
  }
}
