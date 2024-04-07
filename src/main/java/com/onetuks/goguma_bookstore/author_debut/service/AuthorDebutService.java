package com.onetuks.goguma_bookstore.author_debut.service;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author_debut.model.Author;
import com.onetuks.goguma_bookstore.author_debut.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author_debut.service.dto.param.AuthorDebutCreateParam;
import com.onetuks.goguma_bookstore.author_debut.service.dto.result.AuthorDebutCreateResult;
import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorDebutService {

  private final AuthorJpaRepository authorJpaRepository;
  private final MemberRepository memberRepository;

  private final S3Service s3Service;
  private final FileURIProviderService fileURIProviderService;

  public AuthorDebutService(
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
  public AuthorDebutCreateResult createAuthorDebut(long loginId, AuthorDebutCreateParam param) {
    Author temporaryAuthor =
        authorJpaRepository.save(
            Author.builder()
                .member(getUserMember(loginId))
                .profileImgUri(fileURIProviderService.provideDefaultProfileURI())
                .nickname(param.nickname())
                .introduction(param.introduction())
                .build());

    return AuthorDebutCreateResult.from(temporaryAuthor);
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
}
