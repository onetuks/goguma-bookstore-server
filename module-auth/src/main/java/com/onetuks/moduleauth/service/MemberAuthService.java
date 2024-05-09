package com.onetuks.moduleauth.service;

import com.onetuks.moduleauth.oauth.dto.UserData;
import com.onetuks.moduleauth.service.dto.MemberCreateResult;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAuthService {

  private final MemberJpaRepository memberJpaRepository;

  public MemberAuthService(MemberJpaRepository memberJpaRepository) {
    this.memberJpaRepository = memberJpaRepository;
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
                        .authInfo(
                            AuthInfo.builder()
                                .name(userData.name())
                                .socialId(userData.socialId())
                                .clientProvider(userData.clientProvider())
                                .roleTypes(userData.roleTypes())
                                .build())
                        .profileImgFilePath(FileWrapper.of().getUri())
                        .build())),
        optionalMember.isEmpty());
  }

  @Transactional
  public void deleteMember(long memberId) {
    memberJpaRepository.deleteById(memberId);

    // todo : s3 파일 삭제
  }
}
