package com.onetuks.coredomain;

import static com.onetuks.coredomain.CustomFilePathFixture.createProfileImgFilePath;
import static com.onetuks.coredomain.util.TestValueProvider.createBusinessNumber;
import static com.onetuks.coredomain.util.TestValueProvider.createInstagramUrl;
import static com.onetuks.coredomain.util.TestValueProvider.createMailOrderSalesNumber;
import static com.onetuks.coredomain.util.TestValueProvider.createNickname;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.model.vo.EnrollmentInfo;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.file.UUIDProvider;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static Author create(long authorId, Member member) {
    return new Author(
        authorId,
        member,
        createProfileImgFilePath(UUIDProvider.provideUUID()),
        createNickname(),
        "유튜브 대통령",
        createInstagramUrl(),
        new EnrollmentInfo(
            createBusinessNumber(),
            createMailOrderSalesNumber(),
            member.authInfo().roles().contains(RoleType.AUTHOR),
            LocalDateTime.now()),
        null);
  }

  public static Author createWithEnrollmentAt(
      long authoId, Member member, LocalDateTime enrollmentAt) {
    return new Author(
        null,
        member,
        createProfileImgFilePath(UUIDProvider.provideUUID()),
        createNickname(),
        "유튜브 대통령",
        createInstagramUrl(),
        new EnrollmentInfo(
            createBusinessNumber(),
            createMailOrderSalesNumber(),
            member.authInfo().roles().contains(RoleType.AUTHOR),
            enrollmentAt),
        null);
  }
}
