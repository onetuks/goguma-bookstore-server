package com.onetuks.modulescm.author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulecommon.util.UUIDProvider;
import com.onetuks.modulecommon.verification.EnrollmentInfoVerifier;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.author.service.AuthorScmService;
import com.onetuks.modulescm.author.service.dto.param.AuthorCreateEnrollmentParam;
import com.onetuks.modulescm.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentJudgeResult;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class AuthorEntityScmServiceTest extends ScmIntegrationTest {

  @Autowired private AuthorScmService authorScmService;
  @Autowired private S3Repository s3Repository;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @MockBean private EnrollmentInfoVerifier enrollmentInfoVerifier;

  private MemberEntity userMemberEntity;
  private MemberEntity authorMemberEntity;

  private AuthorCreateEnrollmentParam param;

  @BeforeEach
  void setUp() {
    userMemberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    authorMemberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    param =
        new AuthorCreateEnrollmentParam(
            "빠선생님" + UUIDProvider.getUUID(),
            "유튜브 대통령",
            "https://www.instagram.com/pannibottle",
            "1234567890",
            "123456789012345678");
  }

  @Test
  @DisplayName("작가 등록 신청을 수행한다.")
  void createAuthorEnrollmentTest() {
    // Given & When
    AuthorCreateEnrollmentResult result =
        authorScmService.createAuthorEnrollment(userMemberEntity.getMemberId(), param);

    // Then
    assertAll(
        "createAuthorDebutTest",
        () -> Assertions.assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> Assertions.assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("이미 작가인 멤버가 입점 신청을 하는 경우 예외를 던진다.")
  void createAuthorEnrollmentExceptionTest() {
    // Given & When & Then
    assertThrows(
        IllegalStateException.class,
        () -> authorScmService.createAuthorEnrollment(authorMemberEntity.getMemberId(), param));
  }

  @Test
  @DisplayName("존재하지 않는 멤버가 입점 신청하는 경우 예외를 던진다.")
  void createAuthorEnrollment_NotExistsMember_ExceptionTest() {
    // Given & When & Then
    assertThrows(
        EntityNotFoundException.class,
        () -> authorScmService.createAuthorEnrollment(1_454_020L, param));
  }

  @Test
  @DisplayName("입점 심사를 승인하면 입점 승인 여부가 참이 되고, 해당 멤버의 역할이 작가가 된다.")
  void updateAuthorEnrollmentJudgeApprovalTest() {
    // Given
    AuthorCreateEnrollmentResult createResult =
        authorScmService.createAuthorEnrollment(userMemberEntity.getMemberId(), param);

    // When
    AuthorEnrollmentJudgeResult result =
        authorScmService.updateAuthorEnrollmentJudge(createResult.authorId());

    // Then
    assertAll(
        () -> assertThat(result.enrollmentPassed()).isTrue(),
        () -> assertThat(result.memberId()).isEqualTo(userMemberEntity.getMemberId()),
        () -> assertThat(result.roleTypes()).contains(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("입점 심사 승인을 취소하면 입점 승인 여부가 거짓이 되고, 해당 멤버의 역할이 독자가 된다.")
  void updateAuthorEnrollmentJudgeDeniedTest() {
    // Given
    AuthorEntity save = authorJpaRepository.save(AuthorFixture.create(authorMemberEntity));

    // When
    AuthorEnrollmentJudgeResult result =
        authorScmService.updateAuthorEnrollmentJudge(save.getAuthorId());

    // Then
    assertAll(
        () -> assertThat(result.enrollmentPassed()).isFalse(),
        () -> assertThat(result.memberId()).isEqualTo(authorMemberEntity.getMemberId()),
        () -> assertThat(result.roleTypes()).doesNotContain(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("입점 심사를 취소하면 해당 작가 정보가 모두 말소된다. 프로필 이미지 파일도 제거된다.")
  void deleteAuthorEnrollmentTest() {
    // Given
    AuthorEntity save = authorJpaRepository.save(AuthorFixture.create(authorMemberEntity));

    // When
    authorScmService.deleteAuthorEnrollment(authorMemberEntity.getMemberId());

    // Then
    boolean result = authorJpaRepository.existsById(save.getAuthorId());

    assertThat(result).isFalse();
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(save.getProfileImgUrl()));
  }

  @Test
  @DisplayName("존재하지 않는 작가 입점 심사를 취소하면 예외가 발생한다.")
  void deleteAuthorEnrollment_NotExistsAuthor_ExceptionTest() {
    // Given & When
    long notExistsAuthorId = 1_000L;
    Exception e = catchException(() -> authorScmService.deleteAuthorEnrollment(notExistsAuthorId));

    // Then
    assertThat(e).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("작가 아이디와 요청한 아이디가 서로 같은 유저에 대한 정보가 아니면 예외를 던진다. 프로필 이미지는 삭제되지 않는다.")
  void deleteAuthorEnrollment_NotEqualsAuthorAndMember_ExceptionTest() {
    // Given
    AuthorEntity authorEntity = authorJpaRepository.save(AuthorFixture.create(authorMemberEntity));
    authorJpaRepository.flush();

    // 프로필 이미지 등록
    FileWrapper profileImgFile =
        FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.PROFILES);
    s3Repository.putFile(profileImgFile);

    // When & Then
    assertThrows(
        EntityNotFoundException.class,
        () -> authorScmService.deleteAuthorEnrollment(userMemberEntity.getMemberId()));

    File savedProfileImgFile = s3Repository.getFile(profileImgFile.getUri());

    assertThat(savedProfileImgFile).exists();
  }

  @Test
  @DisplayName("요청한 멤버 아이디와 작가 아이디가 같은 유저에 대한 정보이면 임점 심사 정보를 반환한다.")
  void findEnrollmentDetails_Test() {
    // Given
    AuthorCreateEnrollmentResult createResult =
        authorScmService.createAuthorEnrollment(userMemberEntity.getMemberId(), param);

    // When
    AuthorEnrollmentDetailsResult result =
        authorScmService.readAuthorEnrollmentDetails(
            userMemberEntity.getMemberId(), createResult.authorId());

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(createResult.authorId()),
        () -> assertThat(result.memberId()).isEqualTo(userMemberEntity.getMemberId()),
        () -> assertThat(result.roleTypes()).contains(RoleType.USER),
        () -> assertThat(result.enrollmentPassed()).isFalse());
  }

  @Test
  @DisplayName("요청한 멤버 아이디와 작가 아이디가 다른 유저에 대한 정보이면 임점 심사 정보 요청 시 예외를 던진다.")
  void readAuthorEnrollmentDetails_NotSameAuthorAndMember_ExceptionTest() {
    // Given
    AuthorCreateEnrollmentResult createResult =
        authorScmService.createAuthorEnrollment(userMemberEntity.getMemberId(), param);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            authorScmService.readAuthorEnrollmentDetails(
                authorMemberEntity.getMemberId(), createResult.authorId()));
  }

  @Test
  @DisplayName("아직 입점 신청이 완료되지 않은 모든 작가 지망생 상세 정보를 조회한다.")
  void readAllAuthorEnrollmentDetailsTest() {
    // Given
    List<MemberEntity> memberEntities =
        List.of(
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.AUTHOR),
            MemberFixture.create(RoleType.AUTHOR));

    authorJpaRepository.saveAll(
        memberJpaRepository.saveAll(memberEntities).stream().map(AuthorFixture::create).toList());

    // When
    Page<AuthorEnrollmentDetailsResult> results =
        authorScmService.readAllAuthorEnrollmentDetails(PageRequest.of(0, 10));

    // Then
    assertThat(results)
        .hasSize(3)
        .allSatisfy(
            result -> {
              assertThat(result.enrollmentPassed()).isFalse();
              assertThat(result.roleTypes()).contains(RoleType.USER);
            });
  }

  @Test
  @DisplayName("2주간 작가 입점 과정을 지속하지 않은 작가 지망생 정보를 제거한다.")
  void deleteAbandonedAuthorEnrollmentTest() {
    // Given
    List<MemberEntity> memberEntities =
        List.of(
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.AUTHOR),
            MemberFixture.create(RoleType.AUTHOR));

    boolean isTwoWeeksAgo = true;

    for (MemberEntity memberEntity : memberJpaRepository.saveAll(memberEntities)) {
      if (isTwoWeeksAgo) {
        isTwoWeeksAgo = false;
        authorJpaRepository.save(
            AuthorFixture.createWithEnrollmentAt(
                memberEntity, LocalDateTime.now().minusWeeks(2).minusHours(1)));
        continue;
      }

      isTwoWeeksAgo = true;
      authorJpaRepository.save(
          AuthorFixture.createWithEnrollmentAt(
              memberEntity, LocalDateTime.now().minusWeeks(1).plusHours(1)));
    }

    // When
    authorScmService.deleteAbandonedAuthorEnrollment();

    // Then
    Page<AuthorEnrollmentDetailsResult> results =
        authorScmService.readAllAuthorEnrollmentDetails(PageRequest.of(0, 10));

    assertThat(results).hasSize(1);
  }
}
