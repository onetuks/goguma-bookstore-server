package com.onetuks.scmdomain.restock.service;

import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coredomain.restock.repository.RestockScmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestockScmService {

  private final RestockScmRepository restockScmRepository;

  public RestockScmService(RestockScmRepository restockScmRepository) {
    this.restockScmRepository = restockScmRepository;
  }

  @Transactional(readOnly = true)
  public long readRestockBookCount(Long memberId) {
    return restockScmRepository.readCount(memberId);
  }

  @Transactional(readOnly = true)
  public Page<Restock> readAllRestockBooks(long memberId, Pageable pageable) {
    return restockScmRepository.readAllByAuthor(memberId, pageable);
  }
}
