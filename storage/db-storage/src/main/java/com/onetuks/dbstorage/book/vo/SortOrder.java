package com.onetuks.dbstorage.book.vo;

import static com.onetuks.dbstorage.book.entity.QBookEntity.bookEntity;
import static com.onetuks.dbstorage.book.entity.QBookStaticsEntity.bookStaticsEntity;

import com.onetuks.coreobj.enums.book.PageOrder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public enum SortOrder {
  DATE("book.bookId", Direction.DESC, bookEntity.bookId, Order.DESC),
  PRICE_ASC("book.price", Direction.ASC, bookEntity.price, Order.ASC),
  PRICE_DESC("book.price", Direction.DESC, bookEntity.price, Order.DESC),
  SALES("book.salesRate", Direction.DESC, bookStaticsEntity.salesCount, Order.DESC),
  FAVORITE(
      "book.bookStatics.favoriteCount",
      Direction.DESC,
      bookStaticsEntity.favoriteCount,
      Order.DESC),
  VIEW("book.bookStatics.viewCount", Direction.DESC, bookStaticsEntity.viewCount, Order.DESC);

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

  public static SortOrder of(PageOrder pageOrder) {
    return SortOrder.valueOf(pageOrder.name());
  }
}
