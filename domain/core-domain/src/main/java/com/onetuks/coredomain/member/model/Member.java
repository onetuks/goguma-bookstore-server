package com.onetuks.coredomain.member.model;

import com.onetuks.coredomain.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.member.model.vo.AddressInfo;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coredomain.member.model.vo.Nickname;
import lombok.Builder;

@Builder
public record Member(
    Long memberId,
    AuthInfo authInfo,
    Nickname nickname,
    boolean isAlarmPermitted,
    ProfileImgFilePath profileImgFilePath,
    AddressInfo defaultAddressInfo) {

  public Member changeMemberProfile(
      String nickname,
      boolean isAlarmPermitted,
      String profileImgFilePath,
      String address,
      String addressDetail) {
    return new Member(
        memberId(),
        authInfo(),
        new Nickname(nickname),
        isAlarmPermitted,
        new ProfileImgFilePath(profileImgFilePath),
        new AddressInfo(address, addressDetail));
  }

  public Member revokeAuthorRole() {
    return new Member(
        memberId(),
        authInfo().removeAuthorRole(),
        nickname(),
        isAlarmPermitted(),
        profileImgFilePath(),
        defaultAddressInfo());
  }

  public Member grantAuthorRole() {
    return new Member(
        memberId(),
        authInfo().grantAuthorRole(),
        nickname(),
        isAlarmPermitted(),
        profileImgFilePath(),
        defaultAddressInfo());
  }
}
