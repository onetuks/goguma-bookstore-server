package com.onetuks.modulescm.author.service;

import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulecommon.verification.EnrollmentInfoVerifier;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.model.embedded.EnrollmentInfo;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulescm.author.service.dto.param.AuthorCreateEnrollmentParam;
import com.onetuks.modulescm.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentJudgeResult;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorScmService {

  private final AuthorJpaRepository authorJpaRepository;
  private final MemberJpaRepository memberJpaRepository;

  private final S3Repository s3Repository;
  private final EnrollmentInfoVerifier enrollmentInfoVerifier;

  public AuthorScmService(
      AuthorJpaRepository authorJpaRepository,
      MemberJpaRepository memberJpaRepository,
      S3Repository s3Repository,
      EnrollmentInfoVerifier enrollmentInfoVerifier) {
    this.authorJpaRepository = authorJpaRepository;
    this.memberJpaRepository = memberJpaRepository;
    this.s3Repository = s3Repository;
    this.enrollmentInfoVerifier = enrollmentInfoVerifier;
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
              LocalDateTime enrollmentAt = author.getEnrollmentAt();
              if (enrollmentAt.isBefore(twoWeeksAgo)) {
                s3Repository.deleteFile(author.getProfileImgUrl());
                authorJpaRepository.delete(author);
              }
            });
  }

  @Transactional
  public AuthorCreateEnrollmentResult createAuthorEnrollment(
      long loginId, AuthorCreateEnrollmentParam param) {
    enrollmentInfoVerifier.verifyEnrollmentInfo(
        param.businessNumber(), param.mailOrderSalesNumber());

    Author temporaryAuthor =
        authorJpaRepository.save(
            Author.builder()
                .member(getUserMemberById(loginId))
                .profileImgFilePath(FileWrapper.of().getUri())
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
    List<RoleType> roleTypes =
        enrollmentJudgeStatus ? member.grantAuthorRole() : member.revokeAuthorRole();

    return new AuthorEnrollmentJudgeResult(enrollmentJudgeStatus, member.getMemberId(), roleTypes);
  }

  @Transactional
  public void deleteAuthorEnrollment(long authorLoginId) {
    Author author = getAuthorByMemberId(authorLoginId);

    s3Repository.deleteFile(author.getProfileImgUrl());

    authorJpaRepository.deleteById(author.getAuthorId());
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

  @Transactional(readOnly = true)
  public Author getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작가입니다."));
  }

  private Member getUserMemberById(long loginId) {
    Member member =
        memberJpaRepository
            .findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

    if (member.getRoleTypes().contains(RoleType.AUTHOR)) {
      throw new IllegalStateException("이미 작가인 멤버입니다.");
    }
    return member;
  }

  private void checkIllegalArgument(Author author, long authorLoginId) {
    if (author.getMember().getMemberId() != authorLoginId) {
      throw new IllegalArgumentException("유효하지 않은 유저가 작가 입점 신청을 진행하고 있습니다.");
    }
  }

  private Author getAuthorByMemberId(long authorLoginId) {
    return authorJpaRepository
        .findByMemberMemberId(authorLoginId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작가입니다."));
  }
}
