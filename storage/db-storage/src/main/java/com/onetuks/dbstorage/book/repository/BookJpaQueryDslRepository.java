package com.onetuks.dbstorage.book.repository;

import static com.onetuks.dbstorage.book.entity.QBookEntity.bookEntity;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.vo.SortOrder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookJpaQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public BookJpaQueryDslRepository(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Transactional(readOnly = true)
  public List<BookEntity> findByConditionsAndOrderByCriterias(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      SortOrder sortOrder) {
    List<BookEntity> bookEntities =
        queryFactory
            .selectFrom(bookEntity)
            .where(
                containsTitle(title),
                containsAuthorNickname(authorNickname),
                containsCategory(category),
                eqOnlyPermission(onlyPromotion),
                eqExceptSoldOut(exceptSoldOut))
            .orderBy(new OrderSpecifier<>(sortOrder.getOrder(), sortOrder.getCriteria()))
//            .offset(pageable.getOffset())
//            .limit(pageable.getPageSize())
            .fetch();

    return bookEntities;

//    return new PageImpl<>(bookEntities, pageable, bookEntities.size());
  }

  private BooleanExpression containsTitle(String title) {
    return title != null ? bookEntity.title.contains(title) : null;
  }

  private BooleanExpression containsAuthorNickname(String authorNickname) {
    return authorNickname != null ? bookEntity.authorNickname.contains(authorNickname) : null;
  }

  private BooleanExpression containsCategory(Category category) {
    return category != null
        ? Expressions.booleanTemplate(
        "CAST(JSON_CONTAINS({0}, {1}) AS INTEGER) = 1",
        bookEntity.categories, "\"" + category + "\"")
        : null;
  }

  private BooleanExpression eqOnlyPermission(boolean onlyPromotion) {
    return onlyPromotion ? bookEntity.isPromotion.isTrue() : null;
  }

  private BooleanExpression eqExceptSoldOut(boolean exceptSoldOut) {
    return exceptSoldOut ? bookEntity.stockCount.gt(0) : null;
  }
}
