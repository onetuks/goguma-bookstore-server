package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.PROFILES;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import java.time.LocalDateTime;
import java.util.Random;

public class AuthorFixture {

  private static final Random random = new Random();

  public static Author create(Member member) {
    return Author.builder()
        .member(member)
        .profileImgFile(CustomFileFixture.createNullFile().toProfileImgFile())
        .nickname("빠선생님" + UUIDProvider.getUUID())
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
        .nickname("빠선생님" + UUIDProvider.getUUID())
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
        "빠선생님" + authorId,
        "유튜브 대통령" + authorId,
        "https://www.instagram.com/pannibottle" + authorId,
        "escrow-service" + authorId + ".pdf",
        "mail-order-sales" + authorId + ".pdf",
        isAuthorMember,
        LocalDateTime.now());
  }

  private static String createBusinessNumber() {
    return String.valueOf(random.nextLong(1_000_000_000L, 9_999_999_999L));
  }

  private static String createMailOrderSalesNumber() {
    return String.valueOf(random.nextLong(1_000_000_000L, 9_999_999_999L));
  }
}
