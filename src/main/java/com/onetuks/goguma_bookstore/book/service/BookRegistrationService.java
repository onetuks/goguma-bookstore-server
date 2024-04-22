package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BookRegistrationService {

  private final BookJpaRepository bookJpaRepository;

  public BookRegistrationService(BookJpaRepository bookJpaRepository) {
    this.bookJpaRepository = bookJpaRepository;
  }

  @Transactional
  public long createBook(Registration registration) {
    log.info("신간등록 검수 완료된 도서가 등록되었습니다.");
    return bookJpaRepository
        .save(
            Book.builder()
                .author(registration.getAuthor())
                .authorNickname(registration.getAuthor().getNickname())
                .bookConceptualInfo(registration.getBookConceptualInfo())
                .bookPhysicalInfo(registration.getBookPhysicalInfo())
                .bookPriceInfo(registration.getBookPriceInfo())
                .publisher(registration.getPublisher())
                .stockCount(registration.getStockCount())
                .coverImgFile(registration.getCoverImgFile())
                .detailImgFiles(registration.getDetailImgFiles())
                .previewFiles(registration.getPreviewFiles())
                .build())
        .getBookId();
  }
}
