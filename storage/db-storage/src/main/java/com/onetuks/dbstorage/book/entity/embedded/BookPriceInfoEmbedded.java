package com.onetuks.dbstorage.book.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BookPriceInfoEmbedded {

  @Column(name = "regular_price", nullable = false)
  private Long regularPrice;

  @Column(name = "purchase_price", nullable = false)
  private Long purchasePrice;

  @Column(name = "promotion", nullable = false)
  private Boolean promotion;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Builder
  public BookPriceInfoEmbedded(Long regularPrice, Long purchasePrice, Boolean promotion, Long stockCount) {
    this.regularPrice = regularPrice;
    this.purchasePrice = purchasePrice;
    this.promotion = promotion;
    this.stockCount = stockCount;
  }

  public BookPriceInfoEmbedded changeStockCount(long stockCount) {
    this.stockCount = stockCount;
    return this;
  }

  public BookPriceInfoEmbedded changeBookPriceInfo(
      long regularPrice, long purchasePrice, boolean promotion) {
    this.regularPrice = regularPrice;
    this.purchasePrice = purchasePrice;
    this.promotion = promotion;
    return this;
  }
}
