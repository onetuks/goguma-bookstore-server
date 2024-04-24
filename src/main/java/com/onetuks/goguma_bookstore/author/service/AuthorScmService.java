package com.onetuks.goguma_bookstore.author.service;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.embedded.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateEnrollmentParam;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import com.onetuks.goguma_bookstore.author.service.verification.EnrollmentInfoVerificationService;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorScmService {

  private final AuthorJpaRepository authorJpaRepository;
  private final MemberJpaRepository memberJpaRepository;

  private final S3Service s3Service;
  private final EnrollmentInfoVerificationService enrollmentInfoVerificationService;

  public AuthorScmService(
      AuthorJpaRepository authorJpaRepository,
      MemberJpaRepository memberJpaRepository,
      S3Service s3Service,
      EnrollmentInfoVerificationService enrollmentInfoVerificationService) {
    this.authorJpaRepository = authorJpaRepository;
    this.memberJpaRepository = memberJpaRepository;
    this.s3Service = s3Service;
    this.enrollmentInfoVerificationService = enrollmentInfoVerificationService;
  }

  /** 매일 오전 4시에 2주간 작가 입점 심사를 통과하지 못한 작가들 삭제 */
  @Scheduled(cron = "0 0 4 * * ?")
  @Transactional
  public void deleteAbandonedAuthorEnrollment() {
    LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
    authorJpaRepository
        .findAll()
        .forEach(
            author -> {
              LocalDateTime enrollmentAt = author.getEnrollmentInfo().getEnrollmentAt();
              if (enrollmentAt.isBefore(twoWeeksAgo)) {
                s3Service.deleteFile(author.getProfileImgFile().getProfileImgUri());
                authorJpaRepository.delete(author);
              }
            });
  }

  @Transactional
  public AuthorCreateEnrollmentResult createAuthorEnrollment(
      long loginId, AuthorCreateEnrollmentParam param) {
    enrollmentInfoVerificationService.verifyEnrollmentInfo(
        param.businessNumber(), param.mailOrderSalesNumber());

    Author temporaryAuthor =
        authorJpaRepository.save(
            Author.builder()
                .member(getUserMemberById(loginId))
                .profileImgFile(CustomFile.of().toProfileImgFile())
                .nickname(param.nickname())
                .introduction(param.introduction())
                .instagramUrl(param.instagramUrl())
                .enrollmentInfo(
                    EnrollmentInfo.builder()
                        .businessNumber(param.businessNumber())
                        .mailOrderSalesNumber(param.mailOrderSalesNumber())
                        .enrollmentPassed(false)
                        .enrollmentAt(LocalDateTime.now())
                        .build())
                .build());

    return AuthorCreateEnrollmentResult.from(temporaryAuthor);
  }

  @Transactional
  public AuthorEnrollmentJudgeResult updateAuthorEnrollmentJudge(long authorId) {
    Author author = getAuthorById(authorId);
    Member member = author.getMember();

    boolean enrollmentJudgeStatus = author.convertEnrollmentJudgeStatus();
    RoleType roleType =
        enrollmentJudgeStatus ? member.grantAuthorRole() : member.revokeAuthorRole();

    return new AuthorEnrollmentJudgeResult(enrollmentJudgeStatus, member.getMemberId(), roleType);
  }

  @Transactional
  public void deleteAuthorEnrollment(long loginAuthorId, long authorId) {
    Author author = getAuthorById(authorId);

    checkIllegalArgument(author, loginAuthorId);

    s3Service.deleteFile(author.getProfileImgFile().getProfileImgUri());

    authorJpaRepository.deleteById(authorId);
  }

  @Transactional(readOnly = true)
  public AuthorEnrollmentDetailsResult readAuthorEnrollmentDetails(
      long loginAuthorId, long authorId) {
    Author author = getAuthorById(authorId);

    checkIllegalArgument(author, loginAuthorId);

    return AuthorEnrollmentDetailsResult.from(author);
  }

  @Transactional(readOnly = true)
  public Page<AuthorEnrollmentDetailsResult> readAllAuthorEnrollmentDetails(Pageable pageable) {
    return authorJpaRepository
        .findAuthorsByEnrollmentInfoEnrollmentPassedFalse(pageable)
        .map(AuthorEnrollmentDetailsResult::from);
  }

  @Transactional
  public AuthorEditResult updateAuthorProfile(
      long loginAuthorId, long authorId, AuthorEditParam authorEditParam, CustomFile customFile) {
    if (loginAuthorId != authorId) {
      throw new IllegalArgumentException("작가 정보를 수정할 권한이 없습니다.");
    }

    s3Service.putFile(customFile);

    return AuthorEditResult.from(
        getAuthorById(authorId)
            .changeProfileImgFile(customFile.toProfileImgFile())
            .changeAuthorProfile(
                authorEditParam.nickname(),
                authorEditParam.introduction(),
                authorEditParam.instagramUrl()));
  }

  private Member getUserMemberById(long loginId) {
    Member member =
        memberJpaRepository
            .findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

    if (member.getRoleType() != RoleType.USER) {
      throw new IllegalStateException("이미 작가인 멤버입니다.");
    }
    return member;
  }

  private Author getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작가입니다."));
  }

  private void checkIllegalArgument(Author author, long loginAuthorId) {
    if (author.getAuthorId() != loginAuthorId) {
      throw new IllegalArgumentException("유효하지 않은 유저가 작가 입점 신청을 진행하고 있습니다.");
    }
  }
}
