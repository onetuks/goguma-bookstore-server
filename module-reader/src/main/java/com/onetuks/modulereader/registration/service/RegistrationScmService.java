package com.onetuks.modulereader.registration.service;

import com.onetuks.modulereader.author.service.AuthorService;
import com.onetuks.modulereader.book.service.BookRegistrationService;
import com.onetuks.modulereader.global.service.S3Service;
import com.onetuks.modulereader.global.vo.file.FileWrapper;
import com.onetuks.modulereader.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulereader.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.modulereader.registration.service.dto.param.RegistrationEditParam;
import com.onetuks.modulereader.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.modulereader.registration.service.dto.result.RegistrationResult;
import com.onetuks.modulepersistence.book.model.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPriceInfo;
import com.onetuks.modulepersistence.registration.model.Registration;
import com.onetuks.modulepersistence.registration.repository.RegistrationJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationScmService {

  private final RegistrationJpaRepository registrationJpaRepository;
  private final AuthorService authorService;
  private final S3Service s3Service;
  private final BookRegistrationService bookRegistrationService;

  public RegistrationScmService(
      RegistrationJpaRepository registrationJpaRepository,
      AuthorService authorService,
      S3Service s3Service,
      BookRegistrationService bookRegistrationService) {
    this.registrationJpaRepository = registrationJpaRepository;
    this.authorService = authorService;
    this.s3Service = s3Service;
    this.bookRegistrationService = bookRegistrationService;
  }

  @Transactional
  public RegistrationResult createRegistration(
      long authorId,
      RegistrationCreateParam param,
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile) {
    checkFileValidity(coverImgFile, detailImgFiles, previewFiles, sampleFile);

    s3Service.putFile(coverImgFile);
    s3Service.putFile(sampleFile);
    detailImgFiles.fileWrappers().forEach(s3Service::putFile);
    previewFiles.fileWrappers().forEach(s3Service::putFile);

    return RegistrationResult.from(
        registrationJpaRepository.save(
            Registration.builder()
                .author(authorService.getAuthorById(authorId))
                .bookConceptualInfo(
                    BookConceptualInfo.builder()
                        .title(param.title())
                        .oneLiner(param.oneLiner())
                        .summary(param.summary())
                        .categories(param.categories())
                        .publisher(param.publisher())
                        .isbn(param.isbn())
                        .build())
                .bookPhysicalInfo(
                    BookPhysicalInfo.builder()
                        .height(param.height())
                        .width(param.width())
                        .coverType(param.coverType())
                        .pageCount(param.pageCount())
                        .build())
                .bookPriceInfo(
                    BookPriceInfo.builder()
                        .regularPrice(param.regularPrice())
                        .purchasePrice(param.purchasePrice())
                        .promotion(param.promotion())
                        .stockCount(param.stockCount())
                        .build())
                .coverImgFilePath(coverImgFile.getUri())
                .detailImgFilePaths(detailImgFiles.getUris())
                .previewFilePaths(previewFiles.getUris())
                .sampleFilePath(sampleFile.getUri())
                .build()));
  }

  @Transactional
  public RegistrationInspectionResult updateRegistrationApprovalInfo(
      long registrationId, boolean approvalResult, String approvalMemo) {
    Registration updatedRegistration =
        getRegistrationById(registrationId).changeApprovalInfo(approvalResult, approvalMemo);

    if (approvalResult) {
      bookRegistrationService.createBook(updatedRegistration);
    }

    return RegistrationInspectionResult.from(updatedRegistration);
  }

  @Transactional
  public RegistrationResult updateRegistration(
      long registrationId,
      RegistrationEditParam param,
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile) {
    Registration registration = getRegistrationById(registrationId);

    replaceIfValidFile(coverImgFile, detailImgFiles, previewFiles, sampleFile, registration);

    return RegistrationResult.from(
        registration.changeRegistration(
            registration
                .getBookConceptualInfo()
                .changeBookConceptualInfo(param.oneLiner(), param.summary(), param.categories()),
            registration
                .getBookPriceInfo()
                .changeBookPriceInfo(param.regularPrice(), param.purchasePrice(), param.promotion())
                .changeStockCount(param.stockCount()),
            coverImgFile.getUri(),
            detailImgFiles.getUris(),
            previewFiles.getUris(),
            sampleFile.getUri()));
  }

  @Transactional
  public void deleteRegistration(long authorId, long registrationId) {
    Registration registration = getRegistrationById(registrationId);

    if (registration.getAuthor().getAuthorId() != authorId) {
      throw new AccessDeniedException("해당 신간등록을 삭제할 권한이 없습니다.");
    }

    s3Service.deleteFile(registration.getCoverImgUrl());
    s3Service.deleteFile(registration.getSampleUrl());
    registration.getDetailImgUrls().forEach(s3Service::deleteFile);
    registration.getPreviewUrls().forEach(s3Service::deleteFile);

    registrationJpaRepository.delete(registration);
  }

  @Transactional(readOnly = true)
  public RegistrationResult readRegistration(long authorId, long registrationId) {
    Registration registration = getRegistrationById(registrationId);

    if (registration.getAuthor().getAuthorId() != authorId) {
      throw new AccessDeniedException("해당 신간등록을 조회할 권한이 없습니다.");
    }

    return RegistrationResult.from(registration);
  }

  @Transactional(readOnly = true)
  public Page<RegistrationResult> readAllRegistrations(Pageable pageable) {
    return registrationJpaRepository.findAll(pageable).map(RegistrationResult::from);
  }

  @Transactional(readOnly = true)
  public Page<RegistrationResult> readAllRegistrationsByAuthor(
      long loginAuthorId, long authorId, Pageable pageable) {
    if (loginAuthorId != authorId) {
      throw new AccessDeniedException("해당 작가의 신간등록을 조회할 권한이 없습니다.");
    }

    return registrationJpaRepository
        .findByAuthorAuthorId(authorId, pageable)
        .map(RegistrationResult::from);
  }

  @Transactional(readOnly = true)
  public Registration getRegistrationByIsbn(String isbn) {
    return registrationJpaRepository
        .findByBookConceptualInfoIsbn(isbn)
        .orElseThrow(() -> new EntityNotFoundException("해당 ISBN을 가진 신간등록이 존재하지 않습니다."));
  }

  private Registration getRegistrationById(long registrationId) {
    return registrationJpaRepository
        .findById(registrationId)
        .orElseThrow(() -> new IllegalArgumentException("해당 신간등록이 존재하지 않습니다."));
  }

  private void checkFileValidity(
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile) {
    if (coverImgFile.isNullFile()
        || sampleFile.isNullFile()
        || detailImgFiles.isEmpty()
        || previewFiles.isEmpty()) {
      throw new IllegalArgumentException("신간 등록에 필요한 파일이 존재하지 않습니다.");
    }
  }

  private void replaceIfValidFile(
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile,
      Registration registration) {
    if (!coverImgFile.isNullFile()) {
      s3Service.putFile(coverImgFile);
    }
    if (!sampleFile.isNullFile()) {
      s3Service.putFile(sampleFile);
    }
    if (!detailImgFiles.isEmpty()) {
      registration.getDetailImgFilePaths().getUrls().forEach(s3Service::deleteFile);
      detailImgFiles.fileWrappers().forEach(s3Service::putFile);
    }
    if (!previewFiles.isEmpty()) {
      registration.getPreviewFilePaths().getUrls().forEach(s3Service::deleteFile);
      previewFiles.fileWrappers().forEach(s3Service::putFile);
    }
  }
}
