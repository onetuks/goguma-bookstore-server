package com.onetuks.modulereader.favorite.service.dto.result;

import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.favorite.entity.FavoriteEntity;
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

  public static FavoriteGetResult from(FavoriteEntity favoriteEntity, BookEntity bookEntity) {
    return new FavoriteGetResult(
        favoriteEntity.getFavoriteId(),
        bookEntity.getBookId(),
        bookEntity.getCoverImgUrl(),
        bookEntity.getTitle(),
        bookEntity.getAuthorNickname(),
        bookEntity.getRegularPrice(),
        bookEntity.getPurchasePrice(),
        bookEntity.getCategories());
  }
}
