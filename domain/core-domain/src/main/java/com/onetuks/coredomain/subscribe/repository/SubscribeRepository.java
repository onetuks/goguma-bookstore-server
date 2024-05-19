package com.onetuks.coredomain.subscribe.repository;

import com.onetuks.coredomain.subscribe.model.Subscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository {

  Subscribe create(Subscribe subscribe);

  Subscribe read(long subscribeId);

  boolean readExistence(long memberId, long authorId);

  Page<Subscribe> readAll(long memberId, Pageable pageable);

  void delete(long subscribeId);
}
