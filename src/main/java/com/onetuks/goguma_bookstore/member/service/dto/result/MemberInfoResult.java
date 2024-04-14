package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

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
