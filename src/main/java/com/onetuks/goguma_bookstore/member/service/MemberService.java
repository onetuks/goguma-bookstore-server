package com.onetuks.goguma_bookstore.member.service;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import com.onetuks.goguma_bookstore.member.vo.AuthInfo;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

  private final MemberJpaRepository memberJpaRepository;

  private final FileURIProviderService fileURIProviderService;

  public MemberService(
      MemberJpaRepository memberJpaRepository, FileURIProviderService fileURIProviderService) {
    this.memberJpaRepository = memberJpaRepository;
    this.fileURIProviderService = fileURIProviderService;
  }

  @Transactional
  public MemberCreateResult saveMemberIfNotExists(UserData userData) {
    Optional<Member> optionalMember =
        memberJpaRepository.findByAuthInfoSocialIdAndAuthInfoClientProvider(
            userData.socialId(), userData.clientProvider());

    return MemberCreateResult.from(
        optionalMember.orElseGet(
            () ->
                memberJpaRepository.save(
                    Member.builder()
                        .authInfo(AuthInfo.from(userData))
                        .profileImgUri(fileURIProviderService.provideDefaultProfileURI())
                        .build())),
        optionalMember.isEmpty());
  }
}
