package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.util.RandomValueProvider;
import com.onetuks.modulecommon.util.UUIDProvider;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.entity.embedded.EnrollmentInfo;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static AuthorEntity create(MemberEntity memberEntity) {
    FileWrapper profileImgFile = FileWrapperFixture.createNullFile();

    return AuthorEntity.builder()
        .member(memberEntity)
        .profileImgFilePath(profileImgFile.getUri())
        .nickname(RandomValueProvider.createAuthorNickname() + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .instagramUrl("https://www.instagram.com/pannibottle" + UUIDProvider.getUUID())
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .businessNumber(RandomValueProvider.createBusinessNumber())
                .mailOrderSalesNumber(RandomValueProvider.createMailOrderSalesNumber())
                .enrollmentPassed(memberEntity.getRoleTypes().contains(RoleType.AUTHOR))
                .enrollmentAt(LocalDateTime.now())
                .build())
        .build();
  }

  public static AuthorEntity createWithEnrollmentAt(MemberEntity memberEntity, LocalDateTime enrollmentAt) {
    FileWrapper profileImgFile =
        FileWrapperFixture.createFile(memberEntity.getMemberId(), FileType.PROFILES);

    return AuthorEntity.builder()
        .member(memberEntity)
        .profileImgFilePath(profileImgFile.getUri())
        .nickname(RandomValueProvider.createAuthorNickname() + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .businessNumber(RandomValueProvider.createBusinessNumber())
                .mailOrderSalesNumber(RandomValueProvider.createMailOrderSalesNumber())
                .enrollmentPassed(memberEntity.getRoleTypes().contains(RoleType.AUTHOR))
                .enrollmentAt(enrollmentAt)
                .build())
        .build();
  }
}
