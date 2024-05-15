package com.onetuks.coredomain;

import static com.onetuks.coredomain.CustomFilePathFixture.createProfileImgFilePath;
import static com.onetuks.coredomain.util.TestValueProvider.createAddressInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createAuthInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createNickname;

import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.vo.UUIDProvider;

public class MemberFixture {

  public static Member create(long id, RoleType roleType) {
    return new Member(
        id,
        createAuthInfo(roleType),
        createNickname(),
        true,
        createProfileImgFilePath(UUIDProvider.provideUUID()),
        createAddressInfo());
  }
}
