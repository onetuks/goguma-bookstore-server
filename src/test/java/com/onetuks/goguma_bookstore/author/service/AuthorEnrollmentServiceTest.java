package com.onetuks.goguma_bookstore.author.service;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.MAIL_ORDER_SALES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.EscrowServiceFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.MailOrderSalesFile;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorEnrollmentServiceTest extends IntegrationTest {

  @Autowired private AuthorEnrollmentService authorEnrollmentService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  private Member userMember;
  private Member authorMember;

  @BeforeEach
  void setUp() {
    userMember = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    authorMember = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("작가 등록 신청을 수행한다.")
  void createAuthorEnrollmentTest() {
    // Given
    AuthorCreateParam param = AuthorFixture.createCreationParam();

    // When
    AuthorCreateEnrollmentResult result =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), param);

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
            () -> authorEnrollmentService.createAuthorEnrollment(authorMember.getMemberId(), param))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("구매안전서비스확인증을 부여한다.")
  void updateAuthorEscrowService() {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateEnrollmentResult createResult =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), createParam);
    EscrowServiceFile escrowServiceFile =
        CustomFileFixture.create(createResult.authorId(), FileType.ESCROWS).toEscrowServiceFile();

    // When
    AuthorEscrowServiceHandOverResult result =
        authorEnrollmentService.updateAuthorEscrowService(
            createResult.authorId(), escrowServiceFile);

    // Then
    assertThat(result.escrowServiceUrl()).contains(escrowServiceFile.getEscrowServiceUrl());
  }

  @Test
  @DisplayName("통신판매신고증을 전송한다.")
  void updateAuthorMailOrderSalesTest() {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateEnrollmentResult createResult =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), createParam);
    MailOrderSalesFile mailOrderSalesFile =
        CustomFileFixture.create(createResult.authorId(), MAIL_ORDER_SALES).toMailOrderSalesFile();

    // When
    AuthorMailOrderSalesSubmitResult result =
        authorEnrollmentService.updateAuthorMailOrderSales(
            userMember.getMemberId(), createResult.authorId(), mailOrderSalesFile);

    // Then
    assertThat(result.mailOrderSalesUrl()).contains(mailOrderSalesFile.getMailOrderSalesUrl());
  }

  @Test
  @DisplayName("멤버아이디와 작가아이디가 다른 상태로 통신판매신고증 전송 시 예외를 던진다.")
  void updateAuthorMailOrderSalesExceptionTest() {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateEnrollmentResult createResult =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), createParam);
    MailOrderSalesFile mailOrderSalesFile =
        CustomFileFixture.create(createResult.authorId(), MAIL_ORDER_SALES).toMailOrderSalesFile();

    // When & Then
    assertThatThrownBy(
            () ->
                authorEnrollmentService.updateAuthorMailOrderSales(
                    authorMember.getMemberId(), createResult.authorId(), mailOrderSalesFile))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("입점 심사를 승인하면 입점 승인 여부가 참이 되고, 해당 멤버의 역할이 작가가 된다.")
  void updateAuthorEnrollmentJudgeApprovalTest() {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateEnrollmentResult createResult =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), createParam);

    // When
    AuthorEnrollmentJudgeResult result =
        authorEnrollmentService.updateAuthorEnrollmentJudge(createResult.authorId());

    // Then
    assertAll(
        () -> assertThat(result.enrollmentPassed()).isTrue(),
        () -> assertThat(result.memberId()).isEqualTo(userMember.getMemberId()),
        () -> assertThat(result.roleType()).isEqualTo(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("입점 심사 승인을 취소하면 입점 승인 여부가 거짓이 되고, 해당 멤버의 역할이 독자가 된다.")
  void updateAuthorEnrollmentJudgeDeniedTest() {
    // Given
    Author save = authorJpaRepository.save(AuthorFixture.create(authorMember));

    // When
    AuthorEnrollmentJudgeResult result =
        authorEnrollmentService.updateAuthorEnrollmentJudge(save.getAuthorId());

    // Then
    assertAll(
        () -> assertThat(result.enrollmentPassed()).isFalse(),
        () -> assertThat(result.memberId()).isEqualTo(authorMember.getMemberId()),
        () -> assertThat(result.roleType()).isEqualTo(RoleType.USER));
  }

  @Test
  @DisplayName("입점 심사를 취소하면 해당 작가 정보가 모두 말소된다.")
  void deleteAuthorEnrollmentTest() {
    // Given
    Author save = authorJpaRepository.save(AuthorFixture.create(authorMember));

    // When
    authorEnrollmentService.deleteAuthorEnrollment(authorMember.getMemberId(), save.getAuthorId());

    // Then
    boolean result = authorJpaRepository.existsById(save.getAuthorId());

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 작가 입점 심사를 취소하면 예외가 발생한다.")
  void deleteAuthorEnrollment_NotExistsAuthor_ExceptionTest() {
    // Given & When
    long notExistsAuthorId = 1_000L;
    Exception e =
        catchException(
            () ->
                authorEnrollmentService.deleteAuthorEnrollment(
                    authorMember.getMemberId(), notExistsAuthorId));

    // Then
    assertThat(e).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("작가 아이디와 요청한 아이디가 서로 같은 유저에 대한 정보가 아니면 예외를 던진다.")
  void deleteAuthorEnrollment_NotEqualsAuthorAndMember_ExceptionTest() {
    // Given
    Author save = authorJpaRepository.save(AuthorFixture.create(authorMember));

    // When & Then
    assertThatThrownBy(
            () ->
                authorEnrollmentService.deleteAuthorEnrollment(
                    userMember.getMemberId(), save.getAuthorId()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("요청한 멤버 아이디와 작가 아이디가 같은 유저에 대한 정보이면 임점 심사 정보를 반환한다.")
  void findEnrollmentDetails_Test() {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateEnrollmentResult createResult =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), createParam);

    // When
    AuthorEnrollmentDetailsResult result =
        authorEnrollmentService.findAuthorEnrollmentDetails(
            userMember.getMemberId(), createResult.authorId());

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(createResult.authorId()),
        () -> assertThat(result.memberId()).isEqualTo(userMember.getMemberId()),
        () -> assertThat(result.roleType()).isEqualTo(RoleType.USER),
        () -> assertThat(result.enrollmentPassed()).isFalse());
  }

  @Test
  @DisplayName("요청한 멤버 아이디와 작가 아이디가 다른 유저에 대한 정보이면 임점 심사 정보 요청 시 예외를 던진다.")
  void findAuthorEnrollmentDetails_NotSameAuthorAndMember_ExceptionTest() {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateEnrollmentResult createResult =
        authorEnrollmentService.createAuthorEnrollment(userMember.getMemberId(), createParam);

    // When & Then
    assertThatThrownBy(
            () ->
                authorEnrollmentService.findAuthorEnrollmentDetails(
                    authorMember.getMemberId(), createResult.authorId()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("아직 입점 신청이 완료되지 않은 모든 작가 지망생 상세 정보를 조회한다.")
  void findAllAuthorEnrollmentDetailsTest() {
    // Given
    List<Member> members =
        List.of(
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.AUTHOR),
            MemberFixture.create(RoleType.AUTHOR));

    authorJpaRepository.saveAll(
        memberJpaRepository.saveAll(members).stream().map(AuthorFixture::create).toList());

    // When
    List<AuthorEnrollmentDetailsResult> results =
        authorEnrollmentService.findAllAuthorEnrollmentDetails();

    // Then
    assertThat(results)
        .hasSize(3)
        .allSatisfy(
            result -> {
              assertThat(result.enrollmentPassed()).isFalse();
              assertThat(result.roleType()).isEqualTo(RoleType.USER);
            });
  }

  @Test
  @DisplayName("2주간 작가 입점 과정을 지속하지 않은 작가 지망생 정보를 제거한다.")
  void deleteAbandonedAuthorEnrollmentTest() {
    // Given
    List<Member> members =
        List.of(
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.AUTHOR),
            MemberFixture.create(RoleType.AUTHOR));

    authorJpaRepository.saveAll(
        memberJpaRepository.saveAll(members).stream()
            .map(
                member ->
                    AuthorFixture.createWithEnrollmentAt(
                        member, LocalDateTime.now().minusWeeks(2).minusHours(1)))
            .toList());

    // When
    authorEnrollmentService.deleteAbandonedAuthorEnrollment();

    // Then
    List<AuthorEnrollmentDetailsResult> results =
        authorEnrollmentService.findAllAuthorEnrollmentDetails();

    assertThat(results).isEmpty();
  }
}
