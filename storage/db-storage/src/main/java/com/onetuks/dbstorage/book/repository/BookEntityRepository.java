package com.onetuks.dbstorage.book.repository;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import com.onetuks.dbstorage.author.repository.AuthorStaticsJpaRepository;
import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.vo.SortOrder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BookEntityRepository implements BookRepository, BookScmRepository {

  private final BookJpaRepository jpaRepository;
  private final BookJpaQueryDslRepository queryDslRepository;
  private final AuthorStaticsJpaRepository authorStaticsJpaRepository;
  private final BookConverter converter;

  public BookEntityRepository(
      BookJpaRepository jpaRepository,
      BookJpaQueryDslRepository queryDslRepository,
      AuthorStaticsJpaRepository authorStaticsJpaRepository,
      BookConverter converter) {
    this.jpaRepository = jpaRepository;
    this.queryDslRepository = queryDslRepository;
    this.authorStaticsJpaRepository = authorStaticsJpaRepository;
    this.converter = converter;
  }

  @Override
  public Book create(Registration registration) {
    BookEntity bookEntity = converter.toEntity(registration);

    authorStaticsJpaRepository.save(
        bookEntity.getAuthorEntity().getAuthorStaticsEntity().increaseBookCount());

    return converter.toDomain(jpaRepository.save(bookEntity));
  }

  @Override
  public Book read(long bookId) {
    return converter.toDomain(
        jpaRepository
            .findById(bookId)
            .orElseThrow(EntityNotFoundException::new)
            .increaseViewCount());
  }

  @Override
  public Page<Book> readAll(long authorId, Pageable pageable) {
    return jpaRepository.findAll(pageable).map(converter::toDomain);
  }

  @Override
  public Page<Book> readAll(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      PageOrder pageOrder,
      Pageable pageable) {
    return queryDslRepository
        .findByConditionsAndOrderByCriterias(
            title,
            authorNickname,
            category,
            onlyPromotion,
            exceptSoldOut,
            SortOrder.of(pageOrder),
            pageable)
        .map(converter::toDomain);
  }

  @Override
  public Book update(Book book) {
    return converter.toDomain(jpaRepository.save(converter.toEntity(book)));
  }

  @Override
  public void delete(long bookId) {
    jpaRepository
        .findById(bookId)
        .ifPresent(
            bookEntity -> {
              authorStaticsJpaRepository.save(
                  bookEntity.getAuthorEntity().getAuthorStaticsEntity().decreaseBookCount());

              jpaRepository.delete(bookEntity);
            });
  }
}
