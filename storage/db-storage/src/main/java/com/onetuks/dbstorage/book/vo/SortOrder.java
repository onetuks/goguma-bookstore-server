package com.onetuks.dbstorage.book.vo;

import static com.onetuks.dbstorage.book.entity.QBookEntity.bookEntity;

import com.onetuks.coreobj.enums.book.PageOrder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public enum SortOrder {
  PRICE_DESC("book.price", Direction.DESC, bookEntity.price, Order.DESC),
  PRICE_ASC("book.price", Direction.ASC, bookEntity.price, Order.ASC),
  DATE("book.bookId", Direction.DESC, bookEntity.bookId, Order.DESC),
  SALES_COUNT(
      "book.bookStatics.salesCount",
      Direction.DESC,
      bookEntity.bookStaticsEntity.salesCount,
      Order.DESC),
  FAVORITE_COUNT(
      "book.bookStatics.favoriteCount",
      Direction.DESC,
      bookEntity.bookStaticsEntity.favoriteCount,
      Order.DESC),
  VIEW_COUNT(
      "book.bookStatics.viewCount",
      Direction.DESC,
      bookEntity.bookStaticsEntity.viewCount,
      Order.DESC),
  COMMENT_COUNT(
      "book.bookStatics.commentCount",
      Direction.DESC,
      bookEntity.bookStaticsEntity.commentCount,
      Order.DESC),
  REVIEW_SCORE(
      "book.bookStatics.reviewScore",
      Direction.DESC,
      bookEntity.bookStaticsEntity.reviewScore,
      Order.DESC);

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
