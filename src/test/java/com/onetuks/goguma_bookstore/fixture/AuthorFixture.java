package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.PROFILES;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createAuthorNickname;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createBusinessNumber;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createMailOrderSalesNumber;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.embedded.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.util.UUIDProvider;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static Author create(Member member) {
    return Author.builder()
        .member(member)
        .profileImgFile(CustomFileFixture.createNullFile().toProfileImgFile())
        .nickname(createAuthorNickname() + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .instagramUrl("https://www.instagram.com/pannibottle" + UUIDProvider.getUUID())
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .businessNumber(createBusinessNumber())
                .mailOrderSalesNumber(createMailOrderSalesNumber())
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(LocalDateTime.now())
                .build())
        .build();
  }

  public static Author createWithEnrollmentAt(Member member, LocalDateTime enrollmentAt) {
    return Author.builder()
        .member(member)
        .profileImgFile(
            CustomFileFixture.createFile(member.getMemberId(), PROFILES).toProfileImgFile())
        .nickname(createAuthorNickname() + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .businessNumber(createBusinessNumber())
                .mailOrderSalesNumber(createMailOrderSalesNumber())
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
        createAuthorNickname() + authorId,
        "유튜브 대통령" + authorId,
        "https://www.instagram.com/pannibottle" + authorId,
        "escrow-service" + authorId + ".pdf",
        "mail-order-sales" + authorId + ".pdf",
        isAuthorMember,
        LocalDateTime.now());
  }
}
