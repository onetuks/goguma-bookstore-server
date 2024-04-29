package com.onetuks.modulereader.favorite.service.dto.result;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.favorite.model.Favorite;
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
        book.getCoverImgUrl(),
        book.getTitle(),
        book.getAuthorNickname(),
        book.getRegularPrice(),
        book.getPurchasePrice(),
        book.getCategories());
  }
}
