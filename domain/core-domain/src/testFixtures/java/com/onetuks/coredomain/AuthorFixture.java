package com.onetuks.coredomain;

import static com.onetuks.coredomain.CustomFilePathFixture.createProfileImgFilePath;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.model.vo.EnrollmentInfo;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.util.TestValueProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.vo.UUIDProvider;
import java.time.LocalDateTime;

public class AuthorFixture {

  public static Author create(Member member) {
    return new Author(
        null,
        member,
        createProfileImgFilePath(UUIDProvider.provideUUID()),
        TestValueProvider.createAuthorNickname(),
        "유튜브 대통령",
        TestValueProvider.createInstagramUrl(),
        new EnrollmentInfo(
            TestValueProvider.createBusinessNumber(),
            TestValueProvider.createMailOrderSalesNumber(),
            member.authInfo().roles().contains(RoleType.AUTHOR),
            LocalDateTime.now()),
        null
    );
  }

  public static Author createWithEnrollmentAt(Member member, LocalDateTime enrollmentAt) {
    return new Author(
        null,
        member,
        createProfileImgFilePath(UUIDProvider.provideUUID()),
        TestValueProvider.createAuthorNickname(),
        "유튜브 대통령",
        TestValueProvider.createInstagramUrl(),
        new EnrollmentInfo(
            TestValueProvider.createBusinessNumber(),
            TestValueProvider.createMailOrderSalesNumber(),
            member.authInfo().roles().contains(RoleType.AUTHOR),
            enrollmentAt),
        null
    );
  }
}
