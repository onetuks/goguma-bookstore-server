package com.onetuks.goguma_bookstore.book.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookPriceInfo that = (BookPriceInfo) o;
    return Objects.equals(regularPrice, that.regularPrice)
        && Objects.equals(purchasePrice, that.purchasePrice)
        && Objects.equals(promotion, that.promotion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regularPrice, purchasePrice, promotion);
  }
}
