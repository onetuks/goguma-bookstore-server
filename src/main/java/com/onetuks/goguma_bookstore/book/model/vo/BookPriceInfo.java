package com.onetuks.goguma_bookstore.book.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BookPriceInfo {

  @Column(name = "regular_price", nullable = false)
  private Long regularPrice;

  @Column(name = "purchase_price", nullable = false)
  private Long purchasePrice;

  @Column(name = "promotion", nullable = false)
  private Boolean promotion;

  @Builder
  public BookPriceInfo(Long regularPrice, Long purchasePrice, Boolean promotion) {
    this.regularPrice = regularPrice;
    this.purchasePrice = purchasePrice;
    this.promotion = promotion;
  }

  public BookPriceInfo updateBookPriceInfo(
      Long regularPrice, Long purchasePrice, Boolean promotion) {
    this.regularPrice = regularPrice;
    this.purchasePrice = purchasePrice;
    this.promotion = promotion;
    return this;
  }
}
