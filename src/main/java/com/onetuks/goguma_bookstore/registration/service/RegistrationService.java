package com.onetuks.goguma_bookstore.registration.service;

import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

  private final RegistrationJpaRepository registrationJpaRepository;
  private final AuthorService authorService;
  private final S3Service s3Service;

  public RegistrationService(
      RegistrationJpaRepository registrationJpaRepository,
      AuthorService authorService,
      S3Service s3Service) {
    this.registrationJpaRepository = registrationJpaRepository;
    this.authorService = authorService;
    this.s3Service = s3Service;
  }

  @Transactional
  public RegistrationCreateResult createRegistration(
      long authorId,
      RegistrationCreateParam param,
      CustomFile coverImgFile,
      CustomFile sampleFile) {
    if (coverImgFile.isNullFile() || sampleFile.isNullFile()) {
      throw new IllegalArgumentException("신간 등록에 필요한 파일이 존재하지 않습니다.");
    }

    s3Service.putFile(coverImgFile);
    s3Service.putFile(sampleFile);

    return RegistrationCreateResult.from(
        registrationJpaRepository.save(
            Registration.builder()
                .author(authorService.getAuthorById(authorId))
                .coverImgFile(coverImgFile.toCoverImgFile())
                .title(param.title())
                .summary(param.summary())
                .price(param.price())
                .stockCount(param.stockCount())
                .isbn(param.isbn())
                .publisher(param.publisher())
                .promotion(param.promotion())
                .sampleFile(sampleFile.toSampleFile())
                .build()));
  }

  @Transactional
  public RegistrationInspectionResult updateRegistrationApproval(
      long registrationId, RegistrationInspectionParam param) {
    return RegistrationInspectionResult.from(
        getRegistrationById(registrationId)
            .updateApprovalInfo(param.approvalResult(), param.approvalMemo()));
  }

  @Transactional
  public RegistrationEditResult updateRegistration(
      long registrationId,
      RegistrationEditParam param,
      CustomFile coverImgFile,
      CustomFile sampleFile) {
    s3Service.putFile(coverImgFile);
    s3Service.putFile(sampleFile);

    return RegistrationEditResult.from(
        getRegistrationById(registrationId)
            .updateRegistration(
                param.title(),
                param.summary(),
                param.price(),
                param.stockCount(),
                param.isbn(),
                param.publisher(),
                param.promotion(),
                coverImgFile.toCoverImgFile(),
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
  public RegistrationGetResult getRegistration(long authorId, long registrationId) {
    Registration registration = getRegistrationById(registrationId);

    if (registration.getAuthor().getAuthorId() != authorId) {
      throw new AccessDeniedException("해당 신간등록을 조회할 권한이 없습니다.");
    }

    return RegistrationGetResult.from(registration);
  }

  @Transactional(readOnly = true)
  public List<RegistrationGetResult> getAllRegistrations() {
    return registrationJpaRepository.findAll().stream().map(RegistrationGetResult::from).toList();
  }

  @Transactional(readOnly = true)
  public List<RegistrationGetResult> getAllRegistrationsByAuthor(
      long loginAuthorId, long authorId) {
    if (loginAuthorId != authorId) {
      throw new AccessDeniedException("해당 작가의 신간등록을 조회할 권한이 없습니다.");
    }

    return registrationJpaRepository.findByAuthorAuthorId(authorId).stream()
        .map(RegistrationGetResult::from)
        .toList();
  }

  private Registration getRegistrationById(long registrationId) {
    return registrationJpaRepository
        .findById(registrationId)
        .orElseThrow(() -> new IllegalArgumentException("해당 신간등록이 존재하지 않습니다."));
  }
}
