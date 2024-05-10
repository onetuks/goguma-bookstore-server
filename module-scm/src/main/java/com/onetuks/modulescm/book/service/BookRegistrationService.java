package com.onetuks.modulescm.book.service;

import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
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
  public long createBook(RegistrationEntity registrationEntity) {
    log.info("신간등록 검수 완료된 도서가 등록되었습니다.");
    return bookJpaRepository
        .save(
            BookEntity.builder()
                .author(registrationEntity.getAuthorEntity())
                .authorNickname(registrationEntity.getAuthorEntity().getNickname())
                .bookConceptualInfo(registrationEntity.getBookConceptualInfo())
                .bookPhysicalInfo(registrationEntity.getBookPhysicalInfo())
                .bookPriceInfo(registrationEntity.getBookPriceInfo())
                .coverImgFilePath(registrationEntity.getCoverImgUri())
                .detailImgFilePaths(registrationEntity.getDetailImgUris())
                .previewFilePaths(registrationEntity.getPreviewUris())
                .build())
        .getBookId();
  }
}
