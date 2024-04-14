package com.onetuks.goguma_bookstore.registration.service;

import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

  private final RegistrationJpaRepository registrationJpaRepository;
  private final AuthorService authorService;

  public RegistrationService(
      RegistrationJpaRepository registrationJpaRepository, AuthorService authorService) {
    this.registrationJpaRepository = registrationJpaRepository;
    this.authorService = authorService;
  }

  @Transactional
  public RegistrationCreateResult createRegistration(
      long authorId,
      RegistrationCreateParam param,
      CustomFile coverImgFile,
      CustomFile sampleFile) {
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

  public RegistrationInspectionResult updateRegistrationApproval(
      long registrationId, RegistrationInspectionParam param) {
    return RegistrationInspectionResult.from(
        getRegistrationById(registrationId)
            .updateApprovalInfo(param.approvalResult(), param.approvalMemo()));
  }

  private Registration getRegistrationById(long registrationId) {
    return registrationJpaRepository
        .findById(registrationId)
        .orElseThrow(() -> new IllegalArgumentException("해당 신간등록이 존재하지 않습니다."));
  }
}
