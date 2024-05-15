package com.onetuks.dbstorage.favorite.repository;

import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coredomain.favorite.repository.FavoriteRepository;
import com.onetuks.dbstorage.book.repository.BookStaticsJpaRepository;
import com.onetuks.dbstorage.favorite.converter.FavoriteConverter;
import com.onetuks.dbstorage.favorite.entity.FavoriteEntity;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteEntityRepository implements FavoriteRepository {

  private final FavoriteJpaRepository repository;
  private final BookStaticsJpaRepository bookStaticsJpaRepository;
  private final FavoriteConverter converter;

  public FavoriteEntityRepository(
      FavoriteJpaRepository repository,
      BookStaticsJpaRepository bookStaticsJpaRepository,
      FavoriteConverter converter) {
    this.repository = repository;
    this.bookStaticsJpaRepository = bookStaticsJpaRepository;
    this.converter = converter;
  }

  @Override
  public Favorite create(Favorite favorite) {
    FavoriteEntity favoriteEntity = converter.toEntity(favorite);

    bookStaticsJpaRepository.save(
        favoriteEntity.getBookEntity().getBookStaticsEntity().increaseFavoriteCount());

    return converter.toDomain(repository.save(favoriteEntity));
  }

  @Override
  public Favorite read(long favoriteId) {
    return converter.toDomain(
        repository.findById(favoriteId).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public boolean readExistence(long memberId, long bookId) {
    return repository.existsByMemberEntityMemberIdAndBookEntityBookId(memberId, bookId);
  }

  @Override
  public List<Favorite> readAll(long memberId) {
    return repository.findAllByMemberEntityMemberId(memberId).stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public void delete(long favoriteId) {
    repository
        .findById(favoriteId)
        .ifPresent(
            favoriteEntity -> {
              bookStaticsJpaRepository.save(
                  favoriteEntity.getBookEntity().getBookStaticsEntity().decreaseFavoriteCount());

              repository.delete(favoriteEntity);
            });
  }
}
