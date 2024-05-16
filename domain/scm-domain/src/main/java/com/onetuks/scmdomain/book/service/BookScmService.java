package com.onetuks.scmdomain.book.service;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import com.onetuks.coredomain.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.file.repository.FileRepository;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.repository.RegistrationScmRepository;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import com.onetuks.scmdomain.book.param.BookEditParam;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookScmService {

  private final BookScmRepository bookScmRepository;
  private final AuthorScmRepository authorScmRepository;
  private final RegistrationScmRepository registrationScmRepository;
  private final FileRepository fileRepository;

  public BookScmService(
      BookScmRepository bookScmRepository,
      AuthorScmRepository authorScmRepository,
      RegistrationScmRepository registrationScmRepository,
      FileRepository fileRepository) {
    this.bookScmRepository = bookScmRepository;
    this.authorScmRepository = authorScmRepository;
    this.registrationScmRepository = registrationScmRepository;
    this.fileRepository = fileRepository;
  }

  @Transactional(readOnly = true)
  public List<Book> readAllBooksByAuthor(long memberId, long authorId) {
    Author author = authorScmRepository.read(authorId);

    boolean isAdmin = author.member().authInfo().roles().contains(RoleType.ADMIN);
    boolean notAuth = author.member().memberId() != memberId;
    if (!isAdmin && notAuth) {
      throw new ApiAccessDeniedException("해당 도서에 대한 권한이 없는 작가입니다.");
    }

    return bookScmRepository.readAll(authorId);
  }

  @Transactional
  public Book updateBook(
      long memberId,
      long bookId,
      BookEditParam param,
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles) {
    Author author = authorScmRepository.readByMember(memberId);
    Book book = bookScmRepository.read(bookId).changeStockCount(0);

    if (!Objects.equals(book.author().authorId(), author.authorId())) {
      throw new ApiAccessDeniedException("해당 도서에 대한 권한이 없는 멤버입니다.");
    }

    Registration registration =
        registrationScmRepository.readByIsbn(book.bookConceptualInfo().isbn());

    replaceIfValidFile(coverImgFile, detailImgFiles, previewFiles, registration);

    registrationScmRepository.update(
        registration.changeRegistration(
            param.oneLiner(),
            param.summary(),
            param.categories(),
            param.price(),
            param.salesRate(),
            param.isPromotion(),
            param.stockCount(),
            CoverImgFilePath.of(coverImgFile.getUri()),
            DetailImgFilePaths.of(detailImgFiles.getUris()),
            PreviewFilePaths.of(previewFiles.getUris())));

    return bookScmRepository.update(book);
  }

  private void replaceIfValidFile(
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      Registration registration) {
    if (!coverImgFile.isNullFile()) {
      fileRepository.deleteFile(registration.coverImgFilePath().getUrl());
      fileRepository.putFile(coverImgFile);
    }
    if (!detailImgFiles.isEmpty()) {
      registration.detailImgFilePaths().getUrls().forEach(fileRepository::deleteFile);
      detailImgFiles.fileWrappers().forEach(fileRepository::putFile);
    }
    if (!previewFiles.isEmpty()) {
      registration.previewFilePaths().getUrls().forEach(fileRepository::deleteFile);
      previewFiles.fileWrappers().forEach(fileRepository::putFile);
    }
  }
}
