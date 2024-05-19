package com.onetuks.coredomain.author.model;

import com.onetuks.coredomain.author.model.vo.EnrollmentInfo;
import com.onetuks.coredomain.global.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.model.vo.Nickname;

public record Author(
    Long authorId,
    Member member,
    ProfileImgFilePath profileImgFilePath,
    Nickname nickname,
    String introduction,
    String instagramUrl,
    EnrollmentInfo enrollmentInfo,
    AuthorStatics authorStatics) {

  public Author changeAuthorProfile(
      String profileImgFileUri, String nickname, String introduction, String instagramUrl) {
    return new Author(
        authorId(),
        member(),
        new ProfileImgFilePath(profileImgFileUri),
        new Nickname(nickname),
        introduction,
        instagramUrl,
        enrollmentInfo(),
        authorStatics());
  }

  public Author convertEnrollmentPassed(Member member) {
    return new Author(
        authorId(),
        member,
        profileImgFilePath(),
        nickname(),
        introduction(),
        instagramUrl(),
        enrollmentInfo().convertEnrollmentPassed(),
        authorStatics());
  }
}
