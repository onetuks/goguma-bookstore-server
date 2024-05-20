package com.onetuks.scmdomain.restock.service;

import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestockScmService {

  private final AuthorScmRepository authorScmRepository;
  private final BookScmRepository bookScmRepository;

  public RestockScmService(
      AuthorScmRepository authorScmRepository, BookScmRepository bookScmRepository) {
    this.authorScmRepository = authorScmRepository;
    this.bookScmRepository = bookScmRepository;
  }

  @Transactional(readOnly = true)
  public long readRestockBookCount(Long memberId) {
    return bookScmRepository
        .readAll(authorScmRepository.readByMember(memberId).authorId(), Pageable.unpaged())
        .stream()
        .filter(book -> book.bookStatics().restockCount() > 0)
        .count();
  }

  @Transactional(readOnly = true)
  public Page<Book> readAllRestockBooks(long memberId, Pageable pageable) {
    return bookScmRepository.readAll(
        authorScmRepository.readByMember(memberId).authorId(), pageable);
  }
}
