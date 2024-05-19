package com.onetuks.dbstorage.subscribe.repository;

import com.onetuks.coredomain.subscribe.model.Subscribe;
import com.onetuks.coredomain.subscribe.repository.SubscribeRepository;
import com.onetuks.dbstorage.author.repository.AuthorStaticsJpaRepository;
import com.onetuks.dbstorage.subscribe.converter.SubscribeConverter;
import com.onetuks.dbstorage.subscribe.entity.SubscribeEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class SubscribeEntityRepository implements SubscribeRepository {

  private final SubscribeJpaRepository subscribeJpaRepository;
  private final AuthorStaticsJpaRepository authorStaticsJpaRepository;
  private final SubscribeConverter converter;

  public SubscribeEntityRepository(
      SubscribeJpaRepository subscribeJpaRepository,
      AuthorStaticsJpaRepository authorStaticsJpaRepository,
      SubscribeConverter converter) {
    this.subscribeJpaRepository = subscribeJpaRepository;
    this.authorStaticsJpaRepository = authorStaticsJpaRepository;
    this.converter = converter;
  }

  @Override
  public Subscribe create(Subscribe subscribe) {
    SubscribeEntity subscriveEntity = converter.toEntity(subscribe);

    authorStaticsJpaRepository.save(
        subscriveEntity.getAuthorEntity().getAuthorStaticsEntity().increaseSubscriberCount());

    return converter.toDomain(subscribeJpaRepository.save(subscriveEntity));
  }

  @Override
  public Subscribe read(long subscribeId) {
    return converter.toDomain(
        subscribeJpaRepository.findById(subscribeId).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public boolean readExistence(long memberId, long authorId) {
    return subscribeJpaRepository.existsByMemberEntityMemberIdAndAuthorEntityAuthorId(
        memberId, authorId);
  }

  @Override
  public Page<Subscribe> readAll(long memberId, Pageable pageable) {
    return subscribeJpaRepository
        .findAllByMemberEntityMemberId(memberId, pageable)
        .map(converter::toDomain);
  }

  @Override
  public void delete(long subscribeId) {
    subscribeJpaRepository
        .findById(subscribeId)
        .ifPresent(
            subscribeEntity -> {
              authorStaticsJpaRepository.save(
                  subscribeEntity
                      .getAuthorEntity()
                      .getAuthorStaticsEntity()
                      .decreaseSubscriberCount());

              subscribeJpaRepository.delete(subscribeEntity);
            });
  }
}
