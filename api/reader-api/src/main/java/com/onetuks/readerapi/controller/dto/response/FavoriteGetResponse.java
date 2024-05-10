package com.onetuks.readerapi.controller.dto.response;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulereader.favorite.service.dto.result.FavoriteGetResult;
import java.util.List;
import org.springframework.data.domain.Page;

public record FavoriteGetResponse(
    long favoriteId,
    long bookId,
    String coverImgUrl,
    String title,
    String authorNickname,
    long regularPrice,
    long purchasePrice,
    List<Category> categories) {

  public record FavoriteGetResponses(Page<FavoriteGetResponse> favoriteGetResponsePage) {
    public static FavoriteGetResponses from(Page<FavoriteGetResult> results) {
      return new FavoriteGetResponses(results.map(FavoriteGetResponse::from));
    }
  }

  private static FavoriteGetResponse from(FavoriteGetResult result) {
    return new FavoriteGetResponse(
        result.favoriteId(),
        result.bookId(),
        result.coverImgUrl(),
        result.title(),
        result.authorNickname(),
        result.regularPrice(),
        result.purchasePrice(),
        result.categories());
  }
}
