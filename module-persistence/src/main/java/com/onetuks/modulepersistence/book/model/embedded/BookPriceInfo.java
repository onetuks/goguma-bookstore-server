package com.onetuks.modulepersistence.book.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BookPriceInfo {

  @Column(name = "regular_price", nullable = false)
  private Long regularPrice;

  @Column(name = "purchase_price", nullable = false)
  private Long purchasePrice;

  @Column(name = "promotion", nullable = false)
  private Boolean promotion;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Builder
  public BookPriceInfo(Long regularPrice, Long purchasePrice, Boolean promotion, Long stockCount) {
    this.regularPrice = regularPrice;
    this.purchasePrice = purchasePrice;
    this.promotion = promotion;
    this.stockCount = stockCount;
  }

  public BookPriceInfo changeStockCount(long stockCount) {
    this.stockCount = stockCount;
    return this;
  }

  public BookPriceInfo changeBookPriceInfo(
      long regularPrice, long purchasePrice, boolean promotion) {
    this.regularPrice = regularPrice;
    this.purchasePrice = purchasePrice;
    this.promotion = promotion;
    return this;
  }
}
