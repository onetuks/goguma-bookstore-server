package com.onetuks.goguma_bookstore.registration.service;

import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.book.model.vo.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPriceInfo;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
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

  public RegistrationScmService(
      RegistrationJpaRepository registrationJpaRepository,
      AuthorService authorService,
      S3Service s3Service) {
    this.registrationJpaRepository = registrationJpaRepository;
    this.authorService = authorService;
    this.s3Service = s3Service;
  }

  @Transactional
  public RegistrationEditResult createRegistration(
      long authorId,
      RegistrationEditParam param,
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles,
      CustomFile sampleFile) {
    checkFileValidity(coverImgFile, detailImgFiles, previewFiles, sampleFile);

    s3Service.putFile(coverImgFile);
    s3Service.putFile(sampleFile);
    detailImgFiles.forEach(s3Service::putFile);
    previewFiles.forEach(s3Service::putFile);

    return RegistrationEditResult.from(
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
  public RegistrationInspectionResult updateRegistrationApproval(
      long registrationId, RegistrationInspectionParam param) {
    return RegistrationInspectionResult.from(
        getRegistrationById(registrationId)
            .changeApprovalInfo(param.approvalResult(), param.approvalMemo()));
  }

  @Transactional
  public RegistrationEditResult updateRegistration(
      long registrationId,
      RegistrationEditParam param,
      CustomFile coverImgFile,
      List<CustomFile> detailImgFiles,
      List<CustomFile> previewFiles,
      CustomFile sampleFile) {
    checkFileValidity(coverImgFile, detailImgFiles, previewFiles, sampleFile);

    Registration registration = getRegistrationById(registrationId);
    registration
        .getDetailImgFiles()
        .forEach(detailImgFile -> s3Service.deleteFile(detailImgFile.getDetailImgUri()));
    registration
        .getPreviewFiles()
        .forEach(previewFile -> s3Service.deleteFile(previewFile.getPreviewFileUri()));

    s3Service.putFile(coverImgFile);
    s3Service.putFile(sampleFile);
    detailImgFiles.forEach(s3Service::putFile);
    previewFiles.forEach(s3Service::putFile);

    return RegistrationEditResult.from(
        registration.changeRegistration(
            BookConceptualInfo.builder()
                .title(param.title())
                .oneLiner(param.oneLiner())
                .summary(param.summary())
                .categories(param.categories())
                .isbn(param.isbn())
                .build(),
            BookPhysicalInfo.builder()
                .height(param.height())
                .width(param.width())
                .coverType(param.coverType())
                .pageCount(param.pageCount())
                .build(),
            BookPriceInfo.builder()
                .regularPrice(param.regularPrice())
                .purchasePrice(param.purchasePrice())
                .promotion(param.promotion())
                .build(),
            param.publisher(),
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
  public RegistrationGetResult readRegistration(long authorId, long registrationId) {
    Registration registration = getRegistrationById(registrationId);

    if (registration.getAuthor().getAuthorId() != authorId) {
      throw new AccessDeniedException("해당 신간등록을 조회할 권한이 없습니다.");
    }

    return RegistrationGetResult.from(registration);
  }

  @Transactional(readOnly = true)
  public Page<RegistrationGetResult> readAllRegistrations(Pageable pageable) {
    return registrationJpaRepository.findAll(pageable).map(RegistrationGetResult::from);
  }

  @Transactional(readOnly = true)
  public Page<RegistrationGetResult> readAllRegistrationsByAuthor(
      long loginAuthorId, long authorId, Pageable pageable) {
    if (loginAuthorId != authorId) {
      throw new AccessDeniedException("해당 작가의 신간등록을 조회할 권한이 없습니다.");
    }

    return registrationJpaRepository
        .findByAuthorAuthorId(authorId, pageable)
        .map(RegistrationGetResult::from);
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
}
