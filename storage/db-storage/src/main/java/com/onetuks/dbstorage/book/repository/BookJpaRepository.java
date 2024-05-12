package com.onetuks.dbstorage.book.repository;

import com.onetuks.dbstorage.book.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<BookEntity, Long> {

  Page<BookEntity> findAllByAuthorEntityAuthorId(long authorId, Pageable pageable);
}
