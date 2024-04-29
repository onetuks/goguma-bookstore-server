package com.onetuks.modulereader.member.service.dto.param;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberProfileEditParam(
    String nickname,
    Boolean alarmPermission,
    String defaultAddress,
    String defaultAddressDetail,
    CashReceiptType defaultCashReceiptType,
    String defaultCashReceiptNumber) {}
