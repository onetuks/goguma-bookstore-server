package com.onetuks.dbstorage.fixture;

import static com.onetuks.coredomain.CustomFilePathFixture.createProfileImgFilePath;
import static com.onetuks.coredomain.util.TestValueProvider.createBusinessNumber;
import static com.onetuks.coredomain.util.TestValueProvider.createInstagramUrl;
import static com.onetuks.coredomain.util.TestValueProvider.createMailOrderSalesNumber;
import static com.onetuks.coredomain.util.TestValueProvider.createNickname;

import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.vo.UUIDProvider;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import java.time.LocalDateTime;

public class AuthorEntityFixture {

  public static AuthorEntity create(MemberEntity memberEntity) {
    return new AuthorEntity(
        memberEntity,
        createProfileImgFilePath(UUIDProvider.provideUUID()).getUri(),
        createNickname().nicknameValue(),
        "유튜브 대통령",
        createInstagramUrl(),
        createBusinessNumber(),
        createMailOrderSalesNumber(),
        memberEntity.getRoles().contains(RoleType.AUTHOR),
        LocalDateTime.now(),
        null);
  }

  public static AuthorEntity createWithEnrollmentAt(
      MemberEntity memberEntity, LocalDateTime enrollmentAt) {
    return new AuthorEntity(
        memberEntity,
        createProfileImgFilePath(UUIDProvider.provideUUID()).getUri(),
        createNickname().nicknameValue(),
        "유튜브 대통령",
        createInstagramUrl(),
        createBusinessNumber(),
        createMailOrderSalesNumber(),
        memberEntity.getRoles().contains(RoleType.AUTHOR),
        enrollmentAt,
        null);
  }
}
