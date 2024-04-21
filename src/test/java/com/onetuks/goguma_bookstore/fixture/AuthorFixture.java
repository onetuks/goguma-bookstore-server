package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.ESCROWS;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.MAIL_ORDER_SALES;
import static com.onetuks.goguma_bookstore.global.vo.file.FileType.PROFILES;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static Author create(Member member) {
    return Author.builder()
        .member(member)
        .profileImgFile(CustomFileFixture.createNullFile().toProfileImgFile())
        .nickname("빠선생님" + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .escrowServiceFile(
                    CustomFileFixture.createFile(member.getMemberId(), ESCROWS)
                        .toEscrowServiceFile())
                .mailOrderSalesFile(
                    CustomFileFixture.createFile(member.getMemberId(), MAIL_ORDER_SALES)
                        .toMailOrderSalesFile())
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
                .escrowServiceFile(
                    CustomFileFixture.createFile(member.getMemberId(), ESCROWS)
                        .toEscrowServiceFile())
                .mailOrderSalesFile(
                    CustomFileFixture.createFile(member.getMemberId(), MAIL_ORDER_SALES)
                        .toMailOrderSalesFile())
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(enrollmentAt)
                .build())
        .build();
  }

  public static AuthorCreateParam createCreationParam() {
    return new AuthorCreateParam(
        "빠선생님" + UUIDProvider.getUUID(), "유튜브 대통령", "https://www.instagram.com/pannibottle");
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
}
