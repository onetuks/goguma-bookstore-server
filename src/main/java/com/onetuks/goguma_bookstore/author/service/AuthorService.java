package com.onetuks.goguma_bookstore.author.service;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthorService {

  private final AuthorJpaRepository authorJpaRepository;
  private final MemberRepository memberRepository;

  private final S3Service s3Service;
  private final FileURIProviderService fileURIProviderService;

  public AuthorService(
      AuthorJpaRepository authorJpaRepository,
      MemberRepository memberRepository,
      S3Service s3Service,
      FileURIProviderService fileURIProviderService) {
    this.authorJpaRepository = authorJpaRepository;
    this.memberRepository = memberRepository;
    this.s3Service = s3Service;
    this.fileURIProviderService = fileURIProviderService;
  }

  @Transactional
  public AuthorCreateResult createAuthorEnrollment(long loginId, AuthorCreateParam param) {
    Author temporaryAuthor =
        authorJpaRepository.save(
            Author.builder()
                .member(getUserMember(loginId))
                .profileImgUri(fileURIProviderService.provideDefaultProfileURI())
                .nickname(param.nickname())
                .introduction(param.introduction())
                .build());

    return AuthorCreateResult.from(temporaryAuthor);
  }

  @Transactional
  public AuthorEscrowServiceHandOverResult editAuthorEscrowService(
      Long authorId, MultipartFile escrowServiceFile) {
    String uri = fileURIProviderService.provideFileURI(FileType.ESCROWS, authorId);
    s3Service.putFile(uri, escrowServiceFile);

    String escrowServiceUrl = getById(authorId).handOverEscrowService(uri);

    return new AuthorEscrowServiceHandOverResult(escrowServiceUrl);
  }

  @Transactional
  public AuthorMailOrderSalesSubmitResult editAuthorMailOrderSales(
      long loginId, long authorId, MultipartFile mailOrderSalesFile) {
    Author author = getById(authorId);
    if (author.getMember().getMemberId() != loginId) {
      throw new IllegalArgumentException("유효하지 않은 유저가 작가 입점 신청을 진행하고 있습니다.");
    }

    String uri = fileURIProviderService.provideFileURI(FileType.MAIL_ORDER_SALES, authorId);
    s3Service.putFile(uri, mailOrderSalesFile);

    String mailOrderSalesUrl = author.submitMailOrderSales(uri);

    return new AuthorMailOrderSalesSubmitResult(mailOrderSalesUrl);
  }

  private Member getUserMember(long loginId) {
    Member member =
        memberRepository
            .findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));

    if (member.getRoleType() != RoleType.USER) {
      throw new IllegalStateException("이미 작가인 멤버입니다.");
    }
    return member;
  }

  private Author getById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 작가입니다."));
  }
}
