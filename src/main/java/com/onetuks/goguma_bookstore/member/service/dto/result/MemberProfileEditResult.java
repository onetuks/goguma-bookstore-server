package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.modulepersistence.member.model.Member;
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

  public static MemberProfileEditResult from(Member member) {
    return new MemberProfileEditResult(
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
