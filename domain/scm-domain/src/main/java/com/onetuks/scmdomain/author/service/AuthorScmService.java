package com.onetuks.scmdomain.author.service;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.model.vo.EnrollmentInfo;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.file.repository.FileRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.model.vo.Nickname;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.vo.FilePathProvider;
import com.onetuks.scmdomain.author.param.AuthorCreateParam;
import com.onetuks.scmdomain.verification.EnrollmentInfoVerifier;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorScmService {

  private final AuthorScmRepository authorScmRepository;
  private final MemberRepository memberRepository;

  private final FileRepository fileRepository;
  private final EnrollmentInfoVerifier enrollmentInfoVerifier;

  public AuthorScmService(
      AuthorScmRepository authorScmRepository,
      MemberRepository memberRepository,
      FileRepository fileRepository,
      EnrollmentInfoVerifier enrollmentInfoVerifier
  ) {
    this.authorScmRepository = authorScmRepository;
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
    this.enrollmentInfoVerifier = enrollmentInfoVerifier;
  }

  /**
   * 매일 오전 4시에 2주간 작가 입점 심사를 통과하지 못한 작가들 삭제
   */
  @Scheduled(cron = "0 0 4 * * ?")
  @Transactional
  public void deleteAbandonedAuthorEnrollment() {
    LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
    authorScmRepository.readAll()
        .forEach(
            author -> {
              LocalDateTime enrollmentAt = author.enrollmentInfo().enrollmentAt();

              if (enrollmentAt.isBefore(twoWeeksAgo)) {
                fileRepository.deleteFile(author.profileImgFilePath().getUrl());
                authorScmRepository.delete(author.authorId());
              }
            });
  }

  @Transactional
  public Author createAuthor(
      long memberId,
      AuthorCreateParam param
  ) {
    Member member = memberRepository.read(memberId);

    if (member.authInfo().roles().contains(RoleType.AUTHOR)) {
      throw new IllegalArgumentException("이미 작가로 등록되어 있습니다.");
    }

    enrollmentInfoVerifier.verifyEnrollmentInfo(
        param.businessNumber(), param.mailOrderSalesNumber());

    return authorScmRepository.create(
        new Author(
            null,
            member,
            new ProfileImgFilePath(FilePathProvider.provideDefaultProfileURI()),
            new Nickname(param.nickname()),
            param.introduction(),
            param.instagramUrl(),
            new EnrollmentInfo(
                param.businessNumber(),
                param.mailOrderSalesNumber(),
                false,
                LocalDateTime.now()),
            null)
    );
  }

  @Transactional(readOnly = true)
  public Author readAuthorDetails(long memberId, long authorId) {
    Author author = authorScmRepository.read(authorId);
    Member member = memberRepository.read(memberId);

    boolean isAdmin = member.authInfo().roles().contains(RoleType.ADMIN);
    boolean isNotAuthorizedMember = author.member().memberId() != memberId;
    if (!isAdmin && isNotAuthorizedMember) {
      throw new ApiAccessDeniedException("작가 정보를 조회할 수 없습니다.");
    }

    return author;
  }

  @Transactional(readOnly = true)
  public List<Author> readAllAuthorDetails() {
    return authorScmRepository.readAll();
  }

  @Transactional
  public Author updateAuthorEnrollmentPassed(long authorId) {
    Author author = authorScmRepository.read(authorId);
    Member member = memberRepository.update(
        author.enrollmentInfo().isEnrollmentPassed()
            ? author.member().revokeAuthorRole()
            : author.member().grantAuthorRole()
    );

    return authorScmRepository.update(
        author.convertEnrollmentPassed(member));
  }

  @Transactional
  public void deleteAuthor(long memberId) {
    Author author = authorScmRepository.readByMember(memberId);

    fileRepository.deleteFile(author.profileImgFilePath().getUrl());

    authorScmRepository.delete(author.authorId());
  }
}
