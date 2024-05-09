package com.onetuks.modulereader.member.controller.dto.response;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.member.service.dto.result.MemberInfoResult;

public record MemberInfoResponse(
    long memberId,
    String nickname,
    String profileImgUrl,
    boolean alarmPermission,
    String defaultAddress,
    String defaultAddressDetail,
    CashReceiptType defaultCashReceiptType,
    String defaultCashReceiptNumber) {

  public static MemberInfoResponse from(MemberInfoResult result) {
    return new MemberInfoResponse(
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
