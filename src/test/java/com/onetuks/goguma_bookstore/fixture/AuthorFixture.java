package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class AuthorFixture {

  public static Author create(Member member) throws IOException {
    return Author.builder()
        .member(member)
        .profileImgUri(
            MultipartFileFixture.createFile(FileType.PROFILES, member.getMemberId()).getName())
        .nickname("빠선생님")
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .escrowServiceUri(
                    MultipartFileFixture.createFile(FileType.ESCROWS, member.getMemberId())
                        .getName())
                .mailOrderSalesUri(
                    MultipartFileFixture.createFile(FileType.MAIL_ORDER_SALES, member.getMemberId())
                        .getName())
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(LocalDateTime.now())
                .build())
        .build();
  }

  public static Author createWithEnrollmentAt(Member member, LocalDateTime enrollmentAt)
      throws IOException {
    return Author.builder()
        .member(member)
        .profileImgUri(
            MultipartFileFixture.createFile(FileType.PROFILES, member.getMemberId()).getName())
        .nickname("빠선생님")
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .escrowServiceUri(
                    MultipartFileFixture.createFile(FileType.ESCROWS, member.getMemberId())
                        .getName())
                .mailOrderSalesUri(
                    MultipartFileFixture.createFile(FileType.MAIL_ORDER_SALES, member.getMemberId())
                        .getName())
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(enrollmentAt)
                .build())
        .build();
  }

  public static AuthorCreateParam createCreationParam() {
    return new AuthorCreateParam("빠선생님", "유튜브 대통령");
  }

  public static AuthorEnrollmentDetailsResult createDetailsResult() {
    Random random = new Random();

    long authorId = random.nextLong(1_000L);
    boolean isAuthorMember = random.nextBoolean();
    return new AuthorEnrollmentDetailsResult(
        authorId,
        random.nextLong(2_000L),
        isAuthorMember ? RoleType.AUTHOR : RoleType.USER,
        "profile_" + authorId + ".png",
        "빠선생님" + authorId,
        "유튜브 대통령" + authorId,
        "escrow-service" + authorId + ".pdf",
        "mail-order-sales" + authorId + ".pdf",
        isAuthorMember,
        LocalDateTime.now());
  }
}
