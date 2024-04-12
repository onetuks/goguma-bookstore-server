package com.onetuks.goguma_bookstore.member.service;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberEntryInfoParam;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;
import com.onetuks.goguma_bookstore.member.vo.AuthInfo;
import jakarta.persistence.EntityNotFoundException;
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

  @Transactional
  public MemberEntryInfoResult entryMemberInfo(long memberId, MemberEntryInfoParam param) {
    return MemberEntryInfoResult.from(
        getMemberById(memberId)
            .updateNickname(param.nickname())
            .updateAlarmPermission(param.alarmPermission()));
  }

  private Member getMemberById(long memberId) {
    return memberJpaRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
  }
}
