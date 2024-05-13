package com.onetuks.dbstorage.subscribe.repository;

import com.onetuks.coredomain.subscribe.model.Subscribe;
import com.onetuks.coredomain.subscribe.repository.SubscribeRepository;
import com.onetuks.dbstorage.subscribe.converter.SubscribeConverter;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SubscribeEntityRepository implements SubscribeRepository {

  private final SubscribeJpaRepository subscribeJpaRepository;
  private final SubscribeConverter converter;

  public SubscribeEntityRepository(
      SubscribeJpaRepository subscribeJpaRepository,
      SubscribeConverter converter) {
    this.subscribeJpaRepository = subscribeJpaRepository;
    this.converter = converter;
  }

  @Override
  public Subscribe create(Subscribe subscribe) {
    return converter.toDomain(
        subscribeJpaRepository.save(converter.toEntity(subscribe)));
  }

  @Override
  public Subscribe read(long subscribeId) {
    return converter.toDomain(
        subscribeJpaRepository.findById(subscribeId)
            .orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public boolean readExistence(long memberId, long authorId) {
    return subscribeJpaRepository
        .existsByMemberEntityMemberIdAndAuthorEntityAuthorId(memberId, authorId);
  }

  @Override
  public List<Subscribe> readAll(long memberId) {
    return subscribeJpaRepository.findAllByMemberEntityMemberId(memberId)
        .stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public void delete(long subscribeId) {
    subscribeJpaRepository.deleteById(subscribeId);
  }
}
