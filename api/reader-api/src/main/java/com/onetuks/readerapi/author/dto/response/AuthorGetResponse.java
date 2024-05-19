package com.onetuks.readerapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;
import org.springframework.data.domain.Page;

public record AuthorGetResponse(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    long subscribeCount,
    long bookCount,
    long restockCount) {

  public static AuthorGetResponse from(Author author) {
    return new AuthorGetResponse(
        author.authorId(),
        author.profileImgFilePath().getUrl(),
        author.nickname().nicknameValue(),
        author.introduction(),
        author.instagramUrl(),
        author.authorStatics().subscribeCount(),
        author.authorStatics().bookCount(),
        author.authorStatics().restockCount());
  }

  public record AuthorGetReponses(Page<AuthorGetResponse> responses) {

    public static AuthorGetReponses from(Page<Author> results) {
      return new AuthorGetReponses(results.map(AuthorGetResponse::from));
    }
  }
}
