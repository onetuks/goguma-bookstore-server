package com.onetuks.dbstorage.restock.repository;

import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coredomain.restock.repository.RestockRepository;
import com.onetuks.dbstorage.book.repository.BookStaticsJpaRepository;
import com.onetuks.dbstorage.restock.converter.RestockConverter;
import com.onetuks.dbstorage.restock.entity.RestockEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class RestockEntityRepository implements RestockRepository {

  private final RestockJpaRepository repository;
  private final BookStaticsJpaRepository bookStaticsJpaRepository;
  private final RestockConverter converter;

  public RestockEntityRepository(
      RestockJpaRepository repository,
      BookStaticsJpaRepository bookStaticsJpaRepository,
      RestockConverter converter) {
    this.repository = repository;
    this.bookStaticsJpaRepository = bookStaticsJpaRepository;
    this.converter = converter;
  }

  @Override
  public Restock create(Restock restock) {
    RestockEntity restockEntity = converter.toEntity(restock);

    bookStaticsJpaRepository.save(
        restockEntity.getBookEntity().getBookStaticsEntity().increaseRestockCount());

    return converter.toDomain(repository.save(restockEntity));
  }

  @Override
  public Restock read(long restockId) {
    return converter.toDomain(
        repository.findById(restockId).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public Page<Restock> readAll(long memberId, Pageable pageable) {
    return repository.findAllByMemberEntityMemberId(memberId, pageable).map(converter::toDomain);
  }

  @Override
  public Restock update(Restock restock) {
    return converter.toDomain(repository.save(converter.toEntity(restock)));
  }

  @Override
  public void delete(long restockId) {
    bookStaticsJpaRepository.save(
        repository
            .findById(restockId)
            .orElseThrow(EntityNotFoundException::new)
            .getBookEntity()
            .getBookStaticsEntity()
            .decreaseRestockCount());

    repository.deleteById(restockId);
  }
}
