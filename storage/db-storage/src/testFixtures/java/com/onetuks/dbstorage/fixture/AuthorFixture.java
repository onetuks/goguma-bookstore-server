package com.onetuks.dbstorage.fixture;

import com.onetuks.filestorage.vo.FileType;
import com.onetuks.filestorage.vo.FileWrapper;
import com.onetuks.filestorage.fixture.FileWrapperFixture;
import com.onetuks.filestorage.util.RandomValueProvider;
import com.onetuks.filestorage.util.UUIDProvider;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.author.entity.embedded.EnrollmentInfo;
import com.onetuks.dbstorage.global.vo.auth.RoleType;
import com.onetuks.dbstorage.member.entity.MemberEntity;
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
