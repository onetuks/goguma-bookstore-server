package com.onetuks.modulereader.fixture;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.util.RandomValueProvider;
import com.onetuks.modulecommon.util.UUIDProvider;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.model.embedded.EnrollmentInfo;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulereader.author.service.dto.result.AuthorEnrollmentDetailsResult;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static Author create(Member member) {
    FileWrapper profileImgFile = FileWrapperFixture.createNullFile();

    return Author.builder()
        .member(member)
        .profileImgFilePath(profileImgFile.getUri())
        .nickname(RandomValueProvider.createAuthorNickname() + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .instagramUrl("https://www.instagram.com/pannibottle" + UUIDProvider.getUUID())
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .businessNumber(RandomValueProvider.createBusinessNumber())
                .mailOrderSalesNumber(RandomValueProvider.createMailOrderSalesNumber())
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(LocalDateTime.now())
                .build())
        .build();
  }

  public static Author createWithEnrollmentAt(Member member, LocalDateTime enrollmentAt) {
    FileWrapper profileImgFile =
        FileWrapperFixture.createFile(member.getMemberId(), FileType.PROFILES);

    return Author.builder()
        .member(member)
        .profileImgFilePath(profileImgFile.getUri())
        .nickname(RandomValueProvider.createAuthorNickname() + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .businessNumber(RandomValueProvider.createBusinessNumber())
                .mailOrderSalesNumber(RandomValueProvider.createMailOrderSalesNumber())
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(enrollmentAt)
                .build())
        .build();
  }

  public static AuthorEnrollmentDetailsResult createDetailsResult() {
    long authorId = System.currentTimeMillis();
    boolean isAuthorMember = System.currentTimeMillis() % 2 == 0;
    return new AuthorEnrollmentDetailsResult(
        authorId,
        System.currentTimeMillis(),
        isAuthorMember ? RoleType.AUTHOR : RoleType.USER,
        "profile_" + authorId + ".png",
        RandomValueProvider.createAuthorNickname() + authorId,
        "유튜브 대통령" + authorId,
        "https://www.instagram.com/pannibottle" + authorId,
        "escrow-service" + authorId + ".pdf",
        "mail-order-sales" + authorId + ".pdf",
        isAuthorMember,
        LocalDateTime.now());
  }
}
