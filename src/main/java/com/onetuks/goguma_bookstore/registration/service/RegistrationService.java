package com.onetuks.goguma_bookstore.registration.service;

import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationPostParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationPostResult;
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
  public RegistrationPostResult createRegistration(
      long authorId, RegistrationPostParam param, CustomFile coverImgFile, CustomFile sampleFile) {
    return RegistrationPostResult.from(
        registrationJpaRepository.save(
            Registration.builder()
                .author(authorService.getAuthorById(authorId))
                .approvalResult(false)
                .approvalMemo("신간 등록 검수 중입니다.")
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
}
