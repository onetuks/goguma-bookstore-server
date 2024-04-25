package com.onetuks.goguma_bookstore.favorite.service.dto.result;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.vo.Category;
import com.onetuks.goguma_bookstore.favorite.model.Favorite;
import java.util.List;

public record FavoriteGetResult(
    long favoriteId,
    long bookId,
    String coverImgUrl,
    String title,
    String authorNickname,
    long regularPrice,
    long purchasePrice,
    List<Category> categories) {

  public static FavoriteGetResult from(Favorite favorite, Book book) {
    return new FavoriteGetResult(
        favorite.getFavoriteId(),
        book.getBookId(),
        book.getCoverImgFile().getCoverImgUrl(),
        book.getBookConceptualInfo().getTitle(),
        book.getAuthorNickname(),
        book.getBookPriceInfo().getRegularPrice(),
        book.getBookPriceInfo().getPurchasePrice(),
        book.getBookConceptualInfo().getCategories());
  }
}
