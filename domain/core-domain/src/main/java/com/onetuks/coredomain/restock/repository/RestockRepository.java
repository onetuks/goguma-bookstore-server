package com.onetuks.coredomain.restock.repository;

import com.onetuks.coredomain.restock.model.Restock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RestockRepository {

  Restock create(Restock restock);

  Restock read(long restockId);

  Page<Restock> readAll(long memberId, Pageable pageable);

  Restock update(Restock restock);

  void delete(long restockId);
}
