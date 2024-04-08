package com.onetuks.goguma_bookstore.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

class AuthorServiceTest extends IntegrationTest {

  @Autowired private AuthorService authorService;
  @Autowired private MemberRepository memberRepository;

  private Member userMember;
  private Member authorMember;

  @BeforeEach
  void setUp() {
    userMember = memberRepository.save(MemberFixture.create(RoleType.USER));
    authorMember = memberRepository.save(MemberFixture.create(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("작가 등록 신청을 수행한다.")
  void createAuthorEnrollmentTest() {
    // Given
    AuthorCreateParam param = AuthorFixture.createCreationParam();

    // When
    AuthorCreateResult result =
        authorService.createAuthorEnrollment(userMember.getMemberId(), param);

    // Then
    assertAll(
        "createAuthorDebutTest",
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("이미 작가인 멤버가 입점 신청을 하는 경우 예외를 던진다.")
  void createAuthorEnrollmentExceptionTest() {
    // Given
    AuthorCreateParam param = AuthorFixture.createCreationParam();

    // When & Then
    assertThatThrownBy(
            () -> authorService.createAuthorEnrollment(authorMember.getMemberId(), param))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("구매안전서비스확인증을 부여한다.")
  void editAuthorEscrowService() throws IOException {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateResult createResult =
        authorService.createAuthorEnrollment(userMember.getMemberId(), createParam);
    MultipartFile escrowServiceFile =
        MultipartFileFixture.createFile(FileType.ESCROWS, createResult.authorId());

    // When
    AuthorEscrowServiceHandOverResult result =
        authorService.editAuthorEscrowService(createResult.authorId(), escrowServiceFile);

    // Then
    assertThat(result.escrowServiceUrl()).contains(escrowServiceFile.getName());

    MultipartFileFixture.deleteFile(escrowServiceFile.getName());
  }

  @Test
  @DisplayName("통신판매신고증을 전송한다.")
  void editAuthorMailOrderSalesTest() throws IOException {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateResult createResult =
        authorService.createAuthorEnrollment(userMember.getMemberId(), createParam);
    MultipartFile mailOrderSalesFile =
        MultipartFileFixture.createFile(FileType.MAIL_ORDER_SALES, createResult.authorId());

    // When
    AuthorMailOrderSalesSubmitResult result =
        authorService.editAuthorMailOrderSales(
            userMember.getMemberId(), createResult.authorId(), mailOrderSalesFile);

    // Then
    assertThat(result.mailOrderSalesUrl()).contains(mailOrderSalesFile.getName());

    MultipartFileFixture.deleteFile(mailOrderSalesFile.getName());
  }

  @Test
  @DisplayName("멤버아이디와 작가아이디가 다른 상태로 통신판매신고증 전송 시 예외를 던진다.")
  void editAuthorMailOrderSalesExceptionTest() throws IOException {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateResult createResult =
        authorService.createAuthorEnrollment(userMember.getMemberId(), createParam);
    MultipartFile mailOrderSalesFile =
        MultipartFileFixture.createFile(FileType.ESCROWS, createResult.authorId());

    // When & Then
    assertThatThrownBy(
            () ->
                authorService.editAuthorMailOrderSales(
                    authorMember.getMemberId(), createResult.authorId(), mailOrderSalesFile))
        .isInstanceOf(IllegalArgumentException.class);

    MultipartFileFixture.deleteFile(mailOrderSalesFile.getName());
  }
}
