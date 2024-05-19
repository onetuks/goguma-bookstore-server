package com.onetuks.dbstorage.member.converter;

import com.onetuks.coredomain.global.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.model.vo.AddressInfo;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coredomain.member.model.vo.Nickname;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

  public MemberEntity toEntity(Member member) {
    return new MemberEntity(
        member.memberId(),
        member.authInfo().name(),
        member.authInfo().socialId(),
        member.authInfo().clientProvider(),
        member.authInfo().roles(),
        member.nickname().nicknameValue(),
        member.profileImgFilePath().getUri(),
        member.isAlarmPermitted(),
        member.defaultAddressInfo().address(),
        member.defaultAddressInfo().addressDetail());
  }

  public Member toDomain(MemberEntity memberEntity) {
    return new Member(
        memberEntity.getMemberId(),
        new AuthInfo(
            memberEntity.getName(),
            memberEntity.getSocialId(),
            memberEntity.getClientProvider(),
            memberEntity.getRoles()),
        new Nickname(memberEntity.getNickname()),
        memberEntity.getIsAlarmPermitted(),
        new ProfileImgFilePath(memberEntity.getProfileImgUri()),
        new AddressInfo(memberEntity.getDefaultAddress(), memberEntity.getDefaultAddressDetail()));
  }
}
