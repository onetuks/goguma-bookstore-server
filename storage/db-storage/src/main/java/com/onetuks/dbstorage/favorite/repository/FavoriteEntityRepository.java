package com.onetuks.dbstorage.favorite.repository;

import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coredomain.favorite.repository.FavoriteRepository;
import com.onetuks.dbstorage.favorite.converter.FavoriteConverter;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteEntityRepository implements FavoriteRepository {

  private final FavoriteJpaRepository repository;
  private final FavoriteConverter converter;

  public FavoriteEntityRepository(FavoriteJpaRepository repository, FavoriteConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Favorite create(Favorite favorite) {
    return converter.toDomain(
        repository.save(converter.toEntity(favorite)));
  }

  @Override
  public Favorite read(long favoriteId) {
    return converter.toDomain(
        repository.findById(favoriteId)
            .orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public boolean readExistence(long memberId, long bookId) {
    return repository.existsByMemberEntityMemberIdAndBookEntityBookId(memberId, bookId);
  }

  @Override
  public List<Favorite> readAll(long memberId) {
    return repository.findAllByMemberEntityMemberId(memberId)
        .stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public void delete(long favoriteId) {
    repository.deleteById(favoriteId);
  }
}
