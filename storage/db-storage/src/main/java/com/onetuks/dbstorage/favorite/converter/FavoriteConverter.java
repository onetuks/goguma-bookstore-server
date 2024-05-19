package com.onetuks.dbstorage.favorite.converter;

import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.favorite.entity.FavoriteEntity;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import org.springframework.stereotype.Component;

@Component
public class FavoriteConverter {

  private final MemberConverter memberConverter;
  private final BookConverter bookConverter;

  public FavoriteConverter(MemberConverter memberConverter, BookConverter bookConverter) {
    this.memberConverter = memberConverter;
    this.bookConverter = bookConverter;
  }

  public FavoriteEntity toEntity(Favorite favorite) {
    return new FavoriteEntity(
        memberConverter.toEntity(favorite.member()), bookConverter.toEntity(favorite.book()));
  }

  public Favorite toDomain(FavoriteEntity favoriteEntity) {
    return new Favorite(
        favoriteEntity.getFavoriteId(),
        memberConverter.toDomain(favoriteEntity.getMemberEntity()),
        bookConverter.toDomain(favoriteEntity.getBookEntity()));
  }
}
