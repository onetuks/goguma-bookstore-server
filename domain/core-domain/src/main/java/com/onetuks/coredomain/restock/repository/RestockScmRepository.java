package com.onetuks.coredomain.restock.repository;

import com.onetuks.coredomain.restock.model.Restock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RestockScmRepository {

  long readCount(long authorId);

  Page<Restock> readAllByAuthor(long authorId, Pageable pageable);
}
