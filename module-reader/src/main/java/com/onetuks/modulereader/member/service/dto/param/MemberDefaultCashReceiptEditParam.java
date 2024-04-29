package com.onetuks.modulereader.member.service.dto.param;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditParam(
    CashReceiptType defaultCashReceiptType, String defaultCashReceiptNumber) {}
