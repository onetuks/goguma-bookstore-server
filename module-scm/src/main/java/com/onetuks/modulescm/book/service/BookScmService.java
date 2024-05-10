package com.onetuks.modulescm.book.service;

import com.onetuks.modulecommon.error.ErrorCode;
import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulescm.book.service.dto.param.BookEditParam;
import com.onetuks.modulescm.book.service.dto.result.BookEditResult;
import com.onetuks.modulescm.book.service.dto.result.BookResult;
import com.onetuks.modulescm.registration.service.RegistrationScmService;
import com.onetuks.modulescm.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.modulescm.registration.service.dto.result.RegistrationResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookScmService {

  private final BookJpaRepository bookJpaRepository;
  private final RegistrationScmService registrationScmService;

  public BookScmService(
      BookJpaRepository bookJpaRepository, RegistrationScmService registrationScmService) {
    this.bookJpaRepository = bookJpaRepository;
    this.registrationScmService = registrationScmService;
  }

  @Transactional
  public BookEditResult updateBook(
      long authorId,
      long bookId,
      BookEditParam param,
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles) {
    BookEntity bookEntity = getBookById(authorId, bookId).changeStockCount(param.stockCount());

    long registrationId =
        registrationScmService.getRegistrationByIsbn(bookEntity.getIsbn()).getRegistrationId();

    RegistrationInspectionResult inspection =
        registrationScmService.updateRegistrationApprovalInfo(
            registrationId, false, "도서 정보 수정으로 인한 재승인 필요");
    RegistrationResult update =
        registrationScmService.updateRegistration(
            registrationId,
            param.to(),
            coverImgFile,
            detailImgFiles,
            previewFiles,
            FileWrapper.of());

    return BookEditResult.from(bookId, update, inspection);
  }

  @Transactional(readOnly = true)
  public Page<BookResult> getAllBooksByAuthor(
      long loginAuthorId, long authorId, Pageable pageable) {
    if (loginAuthorId != authorId) {
      throw new ApiAccessDeniedException(ErrorCode.UNAUTHORITY_ACCESS_DENIED);
    }

    return bookJpaRepository.findAllByAuthorEntityAuthorId(authorId, pageable).map(BookResult::from);
  }

  private BookEntity getBookById(long authorId, long bookId) {
    BookEntity bookEntity =
        bookJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 도서입니다."));

    if (bookEntity.getAuthorEntity().getAuthorId() != authorId) {
      throw new ApiAccessDeniedException(ErrorCode.UNAUTHORITY_ACCESS_DENIED);
    }

    return bookEntity;
  }
}
