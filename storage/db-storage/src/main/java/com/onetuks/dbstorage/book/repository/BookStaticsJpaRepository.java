package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookStaticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStaticsJpaRepository extends JpaRepository<BookStaticsEntity, Long> {}
