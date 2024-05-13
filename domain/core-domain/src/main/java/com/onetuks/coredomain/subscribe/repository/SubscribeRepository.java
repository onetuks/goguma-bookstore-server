package com.onetuks.coredomain.subscribe.repository;

import com.onetuks.coredomain.subscribe.model.Subscribe;
import java.util.List;

public interface SubscribeRepository {

  Subscribe create(Subscribe subscribe);

  Subscribe read(long subscribeId);

  boolean readExistence(long memberId, long authorId);

  List<Subscribe> readAll(long memberId);

  void delete(long subscribeId);
}
