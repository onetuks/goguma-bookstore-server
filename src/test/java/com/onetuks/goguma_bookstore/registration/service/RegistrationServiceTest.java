package com.onetuks.goguma_bookstore.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.RegistrationFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegistrationServiceTest extends IntegrationTest {

  @Autowired private RegistrationService registrationService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private RegistrationJpaRepository registrationJpaRepository;

  private Author author;

  @BeforeEach
  void setUp() {
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    author = authorJpaRepository.save(AuthorFixture.create(member));
  }

  @Test
  @DisplayName("신간 등록을 요청한다.")
  void createRegistrationTest() {
    // Given
    RegistrationCreateParam param =
        new RegistrationCreateParam("신간 제목", "신간 요약", 10000, 100, "1234567890123", "출판사", true);
    CustomFile coverImgFile = CustomFileFixture.create(author.getAuthorId(), FileType.BOOK_COVERS);
    CustomFile sampleFile = CustomFileFixture.create(author.getAuthorId(), FileType.BOOK_SAMPLES);

    // When
    RegistrationCreateResult result =
        registrationService.createRegistration(
            author.getAuthorId(), param, coverImgFile, sampleFile);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () ->
            assertThat(result.coverImgUrl())
                .isEqualTo(coverImgFile.toCoverImgFile().getCoverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(param.title()),
        () -> assertThat(result.summary()).isEqualTo(param.summary()),
        () -> assertThat(result.price()).isEqualTo(param.price()),
        () -> assertThat(result.stockCount()).isEqualTo(param.stockCount()),
        () -> assertThat(result.isbn()).isEqualTo(param.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(param.publisher()),
        () -> assertThat(result.promotion()).isEqualTo(param.promotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(sampleFile.toSampleFile().getSampleUrl()));
  }

  @Test
  @DisplayName("신간등록을 검수한다.")
  void updateRegistrationApprovalTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));
    RegistrationInspectionParam param = new RegistrationInspectionParam(true, "검수 완료");

    // When
    RegistrationInspectionResult result =
        registrationService.updateRegistrationApproval(save.getRegistrationId(), param);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isTrue(),
        () -> assertThat(result.approvalMemo()).isEqualTo(param.approvalMemo()));
  }
}
