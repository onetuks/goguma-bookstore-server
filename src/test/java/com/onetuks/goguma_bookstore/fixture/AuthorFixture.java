package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.global.service.vo.FileType.ESCROWS;
import static com.onetuks.goguma_bookstore.global.service.vo.FileType.MAIL_ORDER_SALES;
import static com.onetuks.goguma_bookstore.global.service.vo.FileType.PROFILES;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static Author create(Member member) {
    return Author.builder()
        .member(member)
        .profileImgUri(createFileUri(PROFILES, member))
        .nickname("빠선생님" + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .escrowServiceUri(createFileUri(ESCROWS, member))
                .mailOrderSalesUri(createFileUri(MAIL_ORDER_SALES, member))
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(LocalDateTime.now())
                .build())
        .build();
  }

  public static Author createWithEnrollmentAt(Member member, LocalDateTime enrollmentAt) {
    return Author.builder()
        .member(member)
        .profileImgUri(createFileUri(PROFILES, member))
        .nickname("빠선생님" + UUIDProvider.getUUID())
        .introduction("유튜브 대통령")
        .enrollmentInfo(
            EnrollmentInfo.builder()
                .escrowServiceUri(createFileUri(ESCROWS, member))
                .mailOrderSalesUri(createFileUri(MAIL_ORDER_SALES, member))
                .enrollmentPassed(member.getRoleType() == RoleType.AUTHOR)
                .enrollmentAt(enrollmentAt)
                .build())
        .build();
  }

  public static AuthorCreateParam createCreationParam() {
    return new AuthorCreateParam("빠선생님" + UUIDProvider.getUUID(), "유튜브 대통령");
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
        "escrow-service" + authorId + ".pdf",
        "mail-order-sales" + authorId + ".pdf",
        isAuthorMember,
        LocalDateTime.now());
  }

  private static String createFileUri(FileType fileType, Member member) {
    try {
      return MultipartFileFixture.createFile(fileType, member.getMemberId()).getName();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
