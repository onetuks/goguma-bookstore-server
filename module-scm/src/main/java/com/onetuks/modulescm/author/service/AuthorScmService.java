package com.onetuks.modulescm.author.service;

import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulecommon.verification.EnrollmentInfoVerifier;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.entity.embedded.EnrollmentInfo;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
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

    AuthorEntity temporaryAuthorEntity =
        authorJpaRepository.save(
            AuthorEntity.builder()
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

    return AuthorCreateEnrollmentResult.from(temporaryAuthorEntity);
  }

  @Transactional
  public AuthorEnrollmentJudgeResult updateAuthorEnrollmentJudge(long authorId) {
    AuthorEntity authorEntity = getAuthorById(authorId);
    MemberEntity memberEntity = authorEntity.getMemberEntity();

    boolean enrollmentJudgeStatus = authorEntity.convertEnrollmentJudgeStatus();
    List<RoleType> roleTypes =
        enrollmentJudgeStatus ? memberEntity.grantAuthorRole() : memberEntity.revokeAuthorRole();

    return new AuthorEnrollmentJudgeResult(enrollmentJudgeStatus, memberEntity.getMemberId(), roleTypes);
  }

  @Transactional
  public void deleteAuthorEnrollment(long authorLoginId) {
    AuthorEntity authorEntity = getAuthorByMemberId(authorLoginId);

    s3Repository.deleteFile(authorEntity.getProfileImgUrl());

    authorJpaRepository.deleteById(authorEntity.getAuthorId());
  }

  @Transactional(readOnly = true)
  public AuthorEnrollmentDetailsResult readAuthorEnrollmentDetails(
      long loginAuthorId, long authorId) {
    AuthorEntity authorEntity = getAuthorById(authorId);

    checkIllegalArgument(authorEntity, loginAuthorId);

    return AuthorEnrollmentDetailsResult.from(authorEntity);
  }

  @Transactional(readOnly = true)
  public Page<AuthorEnrollmentDetailsResult> readAllAuthorEnrollmentDetails(Pageable pageable) {
    return authorJpaRepository
        .findAuthorsByEnrollmentInfoEnrollmentPassedFalse(pageable)
        .map(AuthorEnrollmentDetailsResult::from);
  }

  @Transactional(readOnly = true)
  public AuthorEntity getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작가입니다."));
  }

  private MemberEntity getUserMemberById(long loginId) {
    MemberEntity memberEntity =
        memberJpaRepository
            .findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

    if (memberEntity.getRoleTypes().contains(RoleType.AUTHOR)) {
      throw new IllegalStateException("이미 작가인 멤버입니다.");
    }
    return memberEntity;
  }

  private void checkIllegalArgument(AuthorEntity authorEntity, long authorLoginId) {
    if (authorEntity.getMemberEntity().getMemberId() != authorLoginId) {
      throw new IllegalArgumentException("유효하지 않은 유저가 작가 입점 신청을 진행하고 있습니다.");
    }
  }

  private AuthorEntity getAuthorByMemberId(long authorLoginId) {
    return authorJpaRepository
        .findByMemberMemberId(authorLoginId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작가입니다."));
  }
}
