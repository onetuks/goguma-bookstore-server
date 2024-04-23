package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.book.service.dto.param.BookEditParam;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookEditResult;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.service.RegistrationScmService;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationResult;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
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
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles) {
    Book book = getBookById(authorId, bookId).changeStockCount(param.stockCount());

    long registrationId =
        registrationScmService
            .getRegistrationByIsbn(book.getBookConceptualInfo().getIsbn())
            .getRegistrationId();
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
            CustomFile.of());

    return BookEditResult.from(bookId, update, inspection);
  }

  private Book getBookById(long authorId, long bookId) {
    Book book =
        bookJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 도서입니다."));

    if (book.getAuthor().getAuthorId() != authorId) {
      throw new AccessDeniedException("현재 작가에게 해당 도서 수정 권한이 없습니다.");
    }

    return book;
  }
}
