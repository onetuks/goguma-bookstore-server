package com.onetuks.coredomain;

import static com.onetuks.coredomain.util.TestValueProvider.createAddressInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createAuthInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createAuthorNickname;

import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.vo.UUIDProvider;

public class MemberFixture {

  public static Member create(RoleType roleType) {
    return new Member(
        null,
        createAuthInfo(roleType),
        createAuthorNickname(),
        true,
        CustomFilePathFixture.createProfileImgFilePath(UUIDProvider.provideUUID()),
        createAddressInfo()
    );
  }
}
