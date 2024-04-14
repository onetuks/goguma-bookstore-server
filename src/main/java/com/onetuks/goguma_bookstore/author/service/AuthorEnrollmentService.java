package com.onetuks.goguma_bookstore.author.service;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthorEnrollmentService {

  private final AuthorJpaRepository authorJpaRepository;
  private final MemberJpaRepository memberJpaRepository;

  private final S3Service s3Service;

  public AuthorEnrollmentService(
      AuthorJpaRepository authorJpaRepository,
      MemberJpaRepository memberJpaRepository,
      S3Service s3Service) {
    this.authorJpaRepository = authorJpaRepository;
    this.memberJpaRepository = memberJpaRepository;
    this.s3Service = s3Service;
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
              if (author.getEnrollmentAt().isBefore(twoWeeksAgo)) {
                try {
                  s3Service.deleteFile(author.getProfileImgFile().getProfileImgUri());
                  authorJpaRepository.delete(author);
                } catch (Exception e) {
                  log.warn("작가 입점 신청 삭제 중 오류가 발생했습니다. - authorId : {}", author.getAuthorId());
                }
              }
            });
  }

  @Transactional
  public AuthorCreateEnrollmentResult createAuthorEnrollment(
      long loginId, AuthorCreateParam param) {
    Author temporaryAuthor =
        authorJpaRepository.save(
            Author.builder()
                .member(getUserMemberById(loginId))
                .profileImgFile(CustomFile.of().toProfileImgFile())
                .nickname(param.nickname())
                .introduction(param.introduction())
                .build());

    return AuthorCreateEnrollmentResult.from(temporaryAuthor);
  }

  @Transactional
  public AuthorEscrowServiceHandOverResult updateAuthorEscrowService(
      Long authorId, CustomFile escrowServiceFile) {
    s3Service.putFile(escrowServiceFile);

    String escrowServiceUrl =
        getAuthorById(authorId).updateEscrowService(escrowServiceFile.toEscrowServiceFile());

    return new AuthorEscrowServiceHandOverResult(escrowServiceUrl);
  }

  @Transactional
  public AuthorMailOrderSalesSubmitResult updateAuthorMailOrderSales(
      long loginAuthorId, long authorId, CustomFile mailOrderSalesFile) {
    Author author = getAuthorById(authorId);

    checkIllegalArgument(author, loginAuthorId);

    s3Service.putFile(mailOrderSalesFile);

    String mailOrderSalesUrl =
        author.updateMailOrderSales(mailOrderSalesFile.toMailOrderSalesFile());

    return new AuthorMailOrderSalesSubmitResult(mailOrderSalesUrl);
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
  public AuthorEnrollmentDetailsResult findAuthorEnrollmentDetails(
      long loginAuthorId, long authorId) {
    Author author = getAuthorById(authorId);

    checkIllegalArgument(author, loginAuthorId);

    return AuthorEnrollmentDetailsResult.from(author);
  }

  @Transactional(readOnly = true)
  public List<AuthorEnrollmentDetailsResult> findAllAuthorEnrollmentDetails() {
    return authorJpaRepository.findAuthorsByEnrollmentInfoEnrollmentPassedFalse().stream()
        .map(AuthorEnrollmentDetailsResult::from)
        .toList();
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
