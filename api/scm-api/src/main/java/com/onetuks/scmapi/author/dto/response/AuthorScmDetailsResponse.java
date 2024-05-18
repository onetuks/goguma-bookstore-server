package com.onetuks.scmapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coreobj.enums.member.RoleType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record AuthorScmDetailsResponse(
    long authorId,
    long memberId,
    List<RoleType> roleTypes,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesUrl,
    boolean enrollmentPassed,
    LocalDateTime enrollmentAt) {

  public static AuthorScmDetailsResponse from(Author author) {
    return new AuthorScmDetailsResponse(
        author.authorId(),
        author.member().memberId(),
        author.member().authInfo().roles(),
        author.profileImgFilePath().getUrl(),
        author.nickname().nicknameValue(),
        author.introduction(),
        author.instagramUrl(),
        author.enrollmentInfo().businessNumber(),
        author.enrollmentInfo().mailOrderSalesNumber(),
        author.enrollmentInfo().isEnrollmentPassed(),
        author.enrollmentInfo().enrollmentAt());
  }

  public record AuthorScmDetailsResponses(Page<AuthorScmDetailsResponse> responses) {

    public static AuthorScmDetailsResponses from(Page<Author> results) {
      return new AuthorScmDetailsResponses(results.map(AuthorScmDetailsResponse::from));
    }
  }
}
