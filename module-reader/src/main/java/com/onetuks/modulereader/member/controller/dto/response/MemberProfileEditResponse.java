package com.onetuks.modulereader.member.controller.dto.response;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.member.service.dto.result.MemberProfileEditResult;

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
