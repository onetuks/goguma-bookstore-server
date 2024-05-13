package com.onetuks.dbstorage.fixture;

import static com.onetuks.coredomain.CustomFilePathFixture.createProfileImgFilePath;
import static com.onetuks.coredomain.util.TestValueProvider.createAddress;
import static com.onetuks.coredomain.util.TestValueProvider.createAddressDetail;
import static com.onetuks.coredomain.util.TestValueProvider.createAuthorNickname;
import static com.onetuks.coredomain.util.TestValueProvider.createClientProvider;
import static com.onetuks.coredomain.util.TestValueProvider.createRoles;
import static com.onetuks.coredomain.util.TestValueProvider.createSocialId;

import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.vo.UUIDProvider;
import com.onetuks.dbstorage.member.entity.MemberEntity;

public class MemberEntityFixture {

  public static MemberEntity create(RoleType roleType) {
    return new MemberEntity(
        "실명",
        createSocialId(),
        createClientProvider(),
        createRoles(roleType),
        createAuthorNickname().nicknameValue(),
        createProfileImgFilePath(UUIDProvider.provideUUID()).getUri(),
        true,
        createAddress(),
        createAddressDetail()
    );
  }
}
