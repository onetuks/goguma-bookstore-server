package com.onetuks.dbstorage.author.converter;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.model.AuthorStatics;
import com.onetuks.coredomain.author.model.vo.EnrollmentInfo;
import com.onetuks.coredomain.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.member.model.vo.Nickname;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.author.entity.AuthorStaticsEntity;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import org.springframework.stereotype.Component;

@Component
public class AuthorConverter {

  private final MemberConverter memberConverter;

  public AuthorConverter(MemberConverter memberConverter) {
    this.memberConverter = memberConverter;
  }

  public AuthorEntity toEntity(Author author) {
    return new AuthorEntity(
        author.authorId(),
        memberConverter.toEntity(author.member()),
        author.profileImgFilePath().getUri(),
        author.nickname().nicknameValue(),
        author.introduction(),
        author.instagramUrl(),
        author.enrollmentInfo().businessNumber(),
        author.enrollmentInfo().mailOrderSalesNumber(),
        author.enrollmentInfo().isEnrollmentPassed(),
        author.enrollmentInfo().enrollmentAt(),
        null);
  }

  public Author toDomain(AuthorEntity authorEntity) {
    return new Author(
        authorEntity.getAuthorId(),
        memberConverter.toDomain(authorEntity.getMemberEntity()),
        new ProfileImgFilePath(authorEntity.getProfileImgUri()),
        new Nickname(authorEntity.getNickname()),
        authorEntity.getIntroduction(),
        authorEntity.getInstagramUrl(),
        new EnrollmentInfo(
            authorEntity.getBusinessNumber(),
            authorEntity.getMailOrderSalesNumber(),
            authorEntity.getIsEnrollmentPassed(),
            authorEntity.getEnrollmentAt()),
        toDomain(authorEntity.getAuthorStaticsEntity()));
  }

  private AuthorStatics toDomain(AuthorStaticsEntity authorStaticsEntity) {
    return new AuthorStatics(
        authorStaticsEntity.getAuthorStaticsId(),
        authorStaticsEntity.getSubscribeCount(),
        authorStaticsEntity.getBookCount(),
        authorStaticsEntity.getRestockCount());
  }
}
