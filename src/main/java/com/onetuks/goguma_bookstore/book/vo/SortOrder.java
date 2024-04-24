package com.onetuks.goguma_bookstore.book.vo;

import static com.onetuks.goguma_bookstore.book.model.QBook.book;
import static com.onetuks.goguma_bookstore.book.model.QBookStatics.bookStatics;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public enum SortOrder {
  DATE("book.bookId", Direction.DESC, book.bookId, Order.DESC),
  PRICE_ASC("book.purchasePrce", Direction.ASC, book.bookPriceInfo.purchasePrice, Order.ASC),
  PRICE_DESC("book.purchasePrce", Direction.DESC, book.bookPriceInfo.purchasePrice, Order.DESC),
  SALES("book.salesRate", Direction.DESC, bookStatics.salesCount, Order.DESC),
  FAVORITE("book.bookStatics.favoriteCount", Direction.DESC, bookStatics.favoriteCount, Order.DESC),
  VIEW("book.bookStatics.viewCount", Direction.DESC, bookStatics.viewCount, Order.DESC),
  SCORE("book.bookStatics.reviewScore", Direction.DESC, bookStatics.reviewScore, Order.DESC);

  private final String property;
  private final Direction direction;
  private final NumberPath<? extends Number> criteria;
  private final Order order;

  SortOrder(
      String property, Direction direction, NumberPath<? extends Number> criteria, Order order) {
    this.property = property;
    this.direction = direction;
    this.criteria = criteria;
    this.order = order;
  }
}
