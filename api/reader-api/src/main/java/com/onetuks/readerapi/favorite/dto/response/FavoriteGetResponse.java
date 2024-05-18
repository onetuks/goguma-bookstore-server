package com.onetuks.readerapi.favorite.dto.response;

import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coreobj.enums.book.Category;
import java.util.List;
import org.springframework.data.domain.Page;

public record FavoriteGetResponse(
    long favoriteId,
    long bookId,
    String title,
    List<Category> categories,
    String authorNickname,
    long regularPrice,
    long purchasePrice,
    String coverImgUrl
) {

  public record FavoriteGetResponses(Page<FavoriteGetResponse> favoriteGetResponsePage) {

    public static FavoriteGetResponses from(Page<Favorite> results) {
      return new FavoriteGetResponses(results.map(FavoriteGetResponse::from));
    }
  }

  private static FavoriteGetResponse from(Favorite favorite) {
    return new FavoriteGetResponse(
        favorite.favoriteId(),
        favorite.book().bookId(),
        favorite.book().bookConceptualInfo().title(),
        favorite.book().bookConceptualInfo().categories(),
        favorite.book().author().nickname().nicknameValue(),
        favorite.book().bookPriceInfo().price(),
        favorite.book().bookPriceInfo().salesRate(),
        favorite.book().coverImgFilePath().getUrl()
    );
  }
}
