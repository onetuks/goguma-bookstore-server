package com.onetuks.modulepersistence.global.vo.order;

import com.onetuks.coreenum.vo.payment.CashReceiptType;
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

  @Enumerated(value = EnumType.STRING)
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
