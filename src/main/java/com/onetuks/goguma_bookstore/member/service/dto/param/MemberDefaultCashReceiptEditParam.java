package com.onetuks.goguma_bookstore.member.service.dto.param;

import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditParam(
    CashReceiptType defaultCashReceiptType, String defaultCashReceiptNumber) {}
