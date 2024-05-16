package com.onetuks.scmdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createBusinessNumber;
import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static com.onetuks.coredomain.util.TestValueProvider.createInstagramUrl;
import static com.onetuks.coredomain.util.TestValueProvider.createMailOrderSalesNumber;
import static com.onetuks.coredomain.util.TestValueProvider.createNickname;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.file.repository.FileRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import com.onetuks.scmdomain.author.param.AuthorCreateParam;
import com.onetuks.scmdomain.author.param.AuthorEditParam;
import com.onetuks.scmdomain.author.service.AuthorScmService;
import com.onetuks.scmdomain.verification.EnrollmentInfoVerifier;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class AuthorScmServiceTest extends ScmDomainIntegrationTest {

  @Autowired private AuthorScmService authorScmService;

  @MockBean private MemberRepository memberRepository;
  @MockBean private AuthorScmRepository authorScmRepository;
  @MockBean private FileRepository fileRepository;
  @MockBean private EnrollmentInfoVerifier enrollmentInfoVerifier;

  @Test
  @DisplayName("2주간 입점 심사 마치지 못한 작가 엔티티 제거에 성공한다.")
  void deleteAbandonedAuthorEnrollmentTest() {
    // Given
    Member notYetAuthorMember1 = MemberFixture.create(createId(), RoleType.USER);
    Author notEnrolledAuthor =
        AuthorFixture.createWithEnrollmentAt(
            createId(), notYetAuthorMember1, LocalDateTime.now().minusWeeks(3));
    Member notYetAuthorMember2 = MemberFixture.create(createId(), RoleType.USER);
    Author enrolledAuthor =
        AuthorFixture.createWithEnrollmentAt(
            createId(), notYetAuthorMember2, LocalDateTime.now().minusWeeks(1));

    given(authorScmRepository.readAll()).willReturn(List.of(notEnrolledAuthor, enrolledAuthor));

    // When
    authorScmService.deleteAbandonedAuthorEnrollment();

    // Then
    verify(fileRepository, times(1)).deleteFile(notEnrolledAuthor.profileImgFilePath().getUrl());
    verify(authorScmRepository, times(1)).delete(notEnrolledAuthor.authorId());
  }

  @Test
  @DisplayName("작가 등록 신청을 수행한다.")
  void createAuthorEnrollmentTest() {
    // Given
    AuthorCreateParam param =
        new AuthorCreateParam(
            createNickname().nicknameValue(),
            "유튜브 대통령",
            createInstagramUrl(),
            createBusinessNumber(),
            createMailOrderSalesNumber());
    Member userMember = MemberFixture.create(createId(), RoleType.USER);
    Author author = AuthorFixture.create(createId(), userMember);

    given(memberRepository.read(userMember.memberId())).willReturn(userMember);
    given(authorScmRepository.create(any())).willReturn(author);

    // When
    Author result = authorScmService.createAuthor(userMember.memberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.enrollmentInfo().isEnrollmentPassed()).isFalse(),
        () -> assertThat(result.member().authInfo().roles()).doesNotContain(RoleType.AUTHOR),
        () -> Assertions.assertThat(result.nickname().nicknameValue()).isEqualTo(param.nickname()),
        () -> Assertions.assertThat(result.introduction()).isEqualTo(param.introduction()));

    verify(enrollmentInfoVerifier, times(1)).verifyEnrollmentInfo(any(), any());
  }

  @Test
  @DisplayName("이미 작가인 멤버가 입점 신청을 하는 경우 예외를 던진다.")
  void createAuthorEnrollmentExceptionTest() {
    // Given
    AuthorCreateParam param =
        new AuthorCreateParam(
            createNickname().nicknameValue(),
            "유튜브 대통령",
            createInstagramUrl(),
            createBusinessNumber(),
            createMailOrderSalesNumber());
    Member authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);

    given(memberRepository.read(authorMember.memberId())).willReturn(authorMember);

    // When & Then
    assertThrows(
        IllegalStateException.class,
        () -> authorScmService.createAuthor(authorMember.memberId(), param));
  }

  @Test
  @DisplayName("존재하지 않는 멤버가 입점 신청하는 경우 예외를 던진다.")
  void createAuthorEnrollment_NotExistsMember_ExceptionTest() {
    // Given
    AuthorCreateParam param =
        new AuthorCreateParam(
            createNickname().nicknameValue(),
            "유튜브 대통령",
            createInstagramUrl(),
            createBusinessNumber(),
            createMailOrderSalesNumber());
    long notExistsMemberId = 1_454_020L;

    given(memberRepository.read(notExistsMemberId)).willThrow(new IllegalArgumentException());

    // Given & When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> authorScmService.createAuthor(notExistsMemberId, param));
  }

  @Test
  @DisplayName("작가 자신의 등록 정보를 조회한다.")
  void readAuthorDetails_AuthorOneSelf_Test() {
    // Given
    Member notYetAuthorMember = MemberFixture.create(createId(), RoleType.USER);
    Author notYetAuthor = AuthorFixture.create(createId(), notYetAuthorMember);

    given(authorScmRepository.read(notYetAuthor.authorId())).willReturn(notYetAuthor);
    given(memberRepository.read(notYetAuthorMember.memberId())).willReturn(notYetAuthorMember);

    // When
    Author result =
        authorScmService.readAuthorDetails(notYetAuthor.authorId(), notYetAuthor.authorId());

    // Then
    assertAll(
        () -> assertThat(result.member()).isEqualTo(notYetAuthorMember),
        () -> assertThat(result.enrollmentInfo().isEnrollmentPassed()).isFalse());
  }

  @Test
  @DisplayName("작가 자신이 아닌 등록 정보 조회 시 예외가 발생한다.")
  void readAuthorDetails_NotAuthorOneSelf_ExceptionTest() {
    // Given
    Member notYetAuthorMember = MemberFixture.create(createId(), RoleType.USER);
    Author notYetAuthor = AuthorFixture.create(createId(), notYetAuthorMember);
    Member notAuthorizedMember = MemberFixture.create(createId(), RoleType.AUTHOR);

    given(authorScmRepository.read(notYetAuthor.authorId())).willReturn(notYetAuthor);
    given(memberRepository.read(notAuthorizedMember.memberId())).willReturn(notAuthorizedMember);

    // When
    assertThatThrownBy(
            () ->
                authorScmService.readAuthorDetails(
                    notAuthorizedMember.memberId(), notYetAuthor.authorId()))
        .isInstanceOf(ApiAccessDeniedException.class);
  }

  @Test
  @DisplayName("관리자는 어떤 작가의 등록 정보도 조회할 수 있다.")
  void readAuthorDetails_Admin_Test() {
    // Given
    Member adminMember = MemberFixture.create(createId(), RoleType.ADMIN);
    Member authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Author author = AuthorFixture.create(createId(), authorMember);

    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(memberRepository.read(adminMember.memberId())).willReturn(adminMember);

    // When
    Author result = authorScmService.readAuthorDetails(adminMember.memberId(), author.authorId());

    // Then
    assertAll(
        () -> assertThat(result.member()).isEqualTo(authorMember),
        () -> assertThat(result.enrollmentInfo().isEnrollmentPassed()).isTrue());
  }

  @Test
  @DisplayName("관리자는 모든 작가 등록 정보를 조회한다.")
  void readAllAuthorDetailsTest() {
    // Given
    Member notYetAuthorMember = MemberFixture.create(createId(), RoleType.USER);
    Member authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Author notYetAuthor = AuthorFixture.create(createId(), notYetAuthorMember);
    Author author = AuthorFixture.create(createId(), authorMember);
    List<Author> allAuthors = List.of(notYetAuthor, author);

    given(authorScmRepository.readAll()).willReturn(allAuthors);

    // When
    List<Author> results = authorScmService.readAllAuthorDetails();

    // Then
    assertThat(results).hasSize(allAuthors.size());
  }

  @Test
  @DisplayName("입점 심사를 승인하면 입점 승인 여부가 참이 되고, 해당 멤버의 역할이 작가가 된다.")
  void updateAuthorEnrollmentPassed_TrueTest() {
    // Given
    Member notYetAuthorMember = MemberFixture.create(createId(), RoleType.USER);
    Member authorMember = MemberFixture.create(notYetAuthorMember.memberId(), RoleType.AUTHOR);
    Author notYetAuthor = AuthorFixture.create(createId(), notYetAuthorMember);

    given(authorScmRepository.read(notYetAuthor.authorId())).willReturn(notYetAuthor);
    given(memberRepository.update(any())).willReturn(authorMember);

    // When
    Author result = authorScmService.updateAuthorEnrollmentPassed(notYetAuthor.authorId());

    // Then
    assertAll(
        () -> assertThat(result.enrollmentInfo().isEnrollmentPassed()).isTrue(),
        () -> assertThat(result.member().authInfo().roles()).contains(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("입점 심사 승인을 취소하면 입점 승인 여부가 거짓이 되고, 해당 멤버의 역할이 독자가 된다.")
  void updateAuthorEnrollmentPassed_FalseTest() {
    // Given
    Member authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Member authorRevokedMember = MemberFixture.create(authorMember.memberId(), RoleType.USER);
    Author author = AuthorFixture.create(createId(), authorMember);

    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(memberRepository.update(any())).willReturn(authorRevokedMember);

    // When
    Author result = authorScmService.updateAuthorEnrollmentPassed(author.authorId());

    // Then
    assertAll(
        () -> assertThat(result.enrollmentInfo().isEnrollmentPassed()).isFalse(),
        () -> assertThat(result.member().authInfo().roles()).doesNotContain(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("작가 프로필 정보를 수정하고, 프로필 이미지는 저장된다.")
  void changeAuthorProfileTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author = AuthorFixture.create(createId(), member);
    AuthorEditParam param =
        new AuthorEditParam(createNickname().nicknameValue(), "유튜브 대통령", createInstagramUrl());
    FileWrapper profileImgFile =
        FileWrapperFixture.createFile(FileType.PROFILES, UUIDProvider.provideUUID());

    given(authorScmRepository.update(any())).willReturn(author);

    // When
    Author result =
        authorScmService.updateAuthorProfile(
            member.memberId(), author.authorId(), param, profileImgFile);

    // Then
    assertAll(
        () -> assertThat(result.profileImgFilePath().getUri()).isEqualTo(profileImgFile.getUri()),
        () -> assertThat(result.nickname().nicknameValue()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(param.instagramUrl()));

    verify(fileRepository, times(1)).deleteFile(author.profileImgFilePath().getUrl());
    verify(fileRepository, times(1)).putFile(profileImgFile);
  }

  @Test
  @DisplayName("권한 없는 멤버가 작가 프로필을 수정하려고 하면 예외를 던진다.")
  void changeAuthorProfile_NotSameAuthorId_ExceptionTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    AuthorEditParam param =
        new AuthorEditParam(createNickname().nicknameValue(), "유튜브 대통령", createInstagramUrl());
    FileWrapper profileImgFile =
        FileWrapperFixture.createFile(FileType.PROFILES, UUIDProvider.provideUUID());

    // When & Then
    assertThatThrownBy(
            () ->
                authorScmService.updateAuthorProfile(
                    member.memberId(), author.authorId(), param, profileImgFile))
        .isInstanceOf(ApiAccessDeniedException.class);

    verify(fileRepository, times(0)).deleteFile(any());
    verify(fileRepository, times(0)).putFile(any());
  }

  @Test
  @DisplayName("입점 심사를 취소하면 해당 작가 정보가 모두 말소된다. 프로필 이미지 파일도 제거된다.")
  void deleteAuthorEnrollmentTest() {
    // Given
    Member authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Author author = AuthorFixture.create(createId(), authorMember);

    given(authorScmRepository.readByMember(authorMember.memberId())).willReturn(author);

    // When
    authorScmService.deleteAuthor(authorMember.memberId());

    // Then
    verify(fileRepository, times(1)).deleteFile(author.profileImgFilePath().getUrl());
    verify(authorScmRepository, times(1)).delete(author.authorId());
  }
}
