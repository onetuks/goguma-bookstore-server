package com.onetuks.dbstorage.book.repository;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.coreobj.enums.book.PageOrder;
import com.onetuks.dbstorage.book.vo.SortOrder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookEntityRepository implements BookRepository, BookScmRepository {

  private final BookJpaRepository jpaRepository;
  private final BookJpaQueryDslRepository queryDslRepository;
  private final BookConverter converter;

  public BookEntityRepository(
      BookJpaRepository jpaRepository,
      BookJpaQueryDslRepository queryDslRepository,
      BookConverter converter) {
    this.jpaRepository = jpaRepository;
    this.queryDslRepository = queryDslRepository;
    this.converter = converter;
  }

  @Override
  public Book create(Registration registration) {
    return converter.toDomain(
        jpaRepository.save(converter.toEntity(registration)));
  }

  @Override
  public Book read(long bookId) {
    return converter.toDomain(
        jpaRepository.findById(bookId)
            .orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public List<Book> readAll(long authorId) {
    return jpaRepository.findAll()
        .stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public List<Book> read(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      PageOrder pageOrder) {
    return queryDslRepository.findByConditionsAndOrderByCriterias(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, SortOrder.of(pageOrder))
        .stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public Book update(Book book) {
    return converter.toDomain(
        jpaRepository.save(converter.toEntity(book)));
  }

  @Override
  public void delete(long bookId) {
    jpaRepository.deleteById(bookId);
  }
}
