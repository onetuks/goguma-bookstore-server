package com.onetuks.goguma_bookstore.registration.service;

import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.book.model.vo.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPriceInfo;
import com.onetuks.goguma_bookstore.book.service.BookRegistrationService;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationResult;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles,
      CustomFile sampleFile) {
    checkFileValidity(coverImgFile, detailImgFiles, previewFiles, sampleFile);

    s3Service.putFile(coverImgFile);
    s3Service.putFile(sampleFile);
    detailImgFiles.forEach(s3Service::putFile);
    previewFiles.forEach(s3Service::putFile);

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
                        .build())
                .publisher(param.publisher())
                .stockCount(param.stockCount())
                .coverImgFile(coverImgFile.toCoverImgFile())
                .detailImgFiles(detailImgFiles.stream().map(CustomFile::toDetailImgFile).toList())
                .previewFiles(previewFiles.stream().map(CustomFile::toPreviewFile).toList())
                .sampleFile(sampleFile.toSampleFile())
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
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles,
      CustomFile sampleFile) {
    Registration registration = getRegistrationById(registrationId);

    replaceIfValidFile(coverImgFile, detailImgFiles, previewFiles, sampleFile, registration);

    return RegistrationResult.from(
        registration.changeRegistration(
            BookConceptualInfo.builder()
                .title(registration.getBookConceptualInfo().getTitle())
                .oneLiner(param.oneLiner())
                .summary(param.summary())
                .categories(param.categories())
                .isbn(registration.getBookConceptualInfo().getIsbn())
                .build(),
            BookPriceInfo.builder()
                .regularPrice(param.regularPrice())
                .purchasePrice(param.purchasePrice())
                .promotion(param.promotion())
                .build(),
            param.stockCount(),
            coverImgFile.toCoverImgFile(),
            detailImgFiles.stream().map(CustomFile::toDetailImgFile).toList(),
            previewFiles.stream().map(CustomFile::toPreviewFile).toList(),
            sampleFile.toSampleFile()));
  }

  @Transactional
  public void deleteRegistration(long authorId, long registrationId) {
    Registration registration = getRegistrationById(registrationId);

    if (registration.getAuthor().getAuthorId() != authorId) {
      throw new AccessDeniedException("해당 신간등록을 삭제할 권한이 없습니다.");
    }

    s3Service.deleteFile(registration.getCoverImgFile().getCoverImgUri());
    s3Service.deleteFile(registration.getSampleFile().getSampleUri());

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
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles,
      CustomFile sampleFile) {
    if (coverImgFile.isNullFile()
        || sampleFile.isNullFile()
        || detailImgFiles.isEmpty()
        || previewFiles.isEmpty()) {
      throw new IllegalArgumentException("신간 등록에 필요한 파일이 존재하지 않습니다.");
    }
  }

  private void replaceIfValidFile(
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles,
      CustomFile sampleFile,
      Registration registration) {
    if (!coverImgFile.isNullFile()) {
      s3Service.putFile(coverImgFile);
    }
    if (!sampleFile.isNullFile()) {
      s3Service.putFile(sampleFile);
    }
    if (!detailImgFiles.isEmpty()) {
      registration
          .getDetailImgFiles()
          .forEach(detailImgFile -> s3Service.deleteFile(detailImgFile.getDetailImgUri()));
      detailImgFiles.forEach(s3Service::putFile);
    }
    if (!previewFiles.isEmpty()) {
      registration
          .getPreviewFiles()
          .forEach(previewFile -> s3Service.deleteFile(previewFile.getPreviewFileUri()));
      previewFiles.forEach(s3Service::putFile);
    }
  }
}
