package com.onetuks.modulepersistence.global.vo.order;

import static jakarta.persistence.EnumType.STRING;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DefaultCashReceiptInfo {

  @Enumerated(value = STRING)
  @Column(name = "default_cash_receipt_type")
  private CashReceiptType defaultCashReceiptType;

  @Column(name = "default_cash_receipt_number")
  private String defaultCashReceiptNumber;

  @Builder
  public DefaultCashReceiptInfo(
      CashReceiptType defaultCashReceiptType, String defaultCashReceiptNumber) {
    this.defaultCashReceiptType = defaultCashReceiptType;
    this.defaultCashReceiptNumber = defaultCashReceiptNumber;
  }
}
