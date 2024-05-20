package com.onetuks.coreobj.enums.book;

public enum PageOrder {
  DATE("book.bookId", "DESC"),
  PRICE_ASC("book.purchasePrce", "ASC"),
  PRICE_DESC("book.purchasePrce", "DESC"),
  SALES("book.salesRate", "DESC"),
  FAVORITE("book.bookStatics.favoriteCount", "DESC"),
  VIEW("book.bookStatics.viewCount", "DESC");

  private final String property;
  private final String order;

  PageOrder(String property, String order) {
    this.property = property;
    this.order = order;
  }
}
