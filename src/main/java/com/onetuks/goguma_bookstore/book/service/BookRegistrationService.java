package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.registration.model.Registration;
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
                .coverImgFilePath(registration.getCoverImgUri())
                .detailImgFilePaths(registration.getDetailImgUris())
                .previewFilePaths(registration.getPreviewUris())
                .build())
        .getBookId();
  }
}
