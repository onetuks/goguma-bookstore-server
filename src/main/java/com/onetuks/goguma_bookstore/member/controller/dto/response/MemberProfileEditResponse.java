package com.onetuks.goguma_bookstore.member.controller.dto.response;

import com.onetuks.goguma_bookstore.member.service.dto.result.MemberProfileEditResult;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberProfileEditResponse(
    long memberId,
    String nickname,
    String profileImgUrl,
    boolean alarmPermission,
    String defaultAddress,
    String defaultAddressDetail,
    CashReceiptType defaultCashReceiptType,
    String defaultCashReceiptNumber) {

  public static MemberProfileEditResponse from(MemberProfileEditResult result) {
    return new MemberProfileEditResponse(
        result.memberId(),
        result.nickname(),
        result.profileImgUrl(),
        result.alarmPermission(),
        result.defaultAddress(),
        result.defaultAddressDetail(),
        result.defaultCashReceiptType(),
        result.defaultCashReceiptNumber());
  }
}
