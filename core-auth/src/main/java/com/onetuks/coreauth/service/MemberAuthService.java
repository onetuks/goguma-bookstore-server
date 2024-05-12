package com.onetuks.coreauth.service;

import com.onetuks.coreauth.service.dto.MemberCreateResult;
import com.onetuks.coreobj.vo.FileWrapper;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.repository.MemberJpaRepository;
import com.onetuks.dbstorage.member.embedded.AuthInfo;
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
  public MemberCreateResult saveMemberIfNotExists(AuthInfo authInfo) {
    Optional<MemberEntity> optionalMember =
        memberJpaRepository.findByAuthInfoSocialIdAndAuthInfoClientProvider(
            authInfo.socialId(), authInfo.clientProvider());

    return MemberCreateResult.from(
        optionalMember.orElseGet(
            () ->
                memberJpaRepository.save(
                    MemberEntity.builder()
                        .authInfo(
                            AuthInfo.builder()
                                .name(authInfo.name())
                                .socialId(authInfo.socialId())
                                .clientProvider(authInfo.clientProvider())
                                .roleTypes(authInfo.roles())
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
