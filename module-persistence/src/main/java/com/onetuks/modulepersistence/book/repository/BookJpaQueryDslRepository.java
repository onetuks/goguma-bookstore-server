package com.onetuks.modulepersistence.book.repository;

import static com.onetuks.modulepersistence.book.model.QBook.book;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.book.vo.SortOrder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BookJpaQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public BookJpaQueryDslRepository(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Transactional(readOnly = true)
  public Page<Book> findByConditionsAndOrderByCriterias(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      SortOrder sortOrder,
      Pageable pageable) {
    List<Book> books =
        queryFactory
            .selectFrom(book)
            .where(
                containsTitle(title),
                containsAuthorNickname(authorNickname),
                containsCategory(category),
                eqOnlyPermission(onlyPromotion),
                eqExceptSoldOut(exceptSoldOut))
            .orderBy(new OrderSpecifier<>(sortOrder.getOrder(), sortOrder.getCriteria()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    return new PageImpl<>(books, pageable, books.size());
  }

  private BooleanExpression containsTitle(String title) {
    return title != null ? book.bookConceptualInfo.title.contains(title) : null;
  }

  private BooleanExpression containsAuthorNickname(String authorNickname) {
    return authorNickname != null ? book.authorNickname.contains(authorNickname) : null;
  }

  private BooleanExpression containsCategory(Category category) {
    return category != null
        ? Expressions.booleanTemplate(
            "CAST(JSON_CONTAINS({0}, {1}) AS INTEGER) = 1",
            book.bookConceptualInfo.categories, "\"" + category + "\"")
        : null;
  }

  private BooleanExpression eqOnlyPermission(boolean onlyPromotion) {
    return onlyPromotion ? book.bookPriceInfo.promotion.isTrue() : null;
  }

  private BooleanExpression eqExceptSoldOut(boolean exceptSoldOut) {
    return exceptSoldOut ? book.bookPriceInfo.stockCount.gt(0) : null;
  }
}
