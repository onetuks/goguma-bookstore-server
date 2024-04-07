package com.onetuks.goguma_bookstore.author_debut.repository;

import com.onetuks.goguma_bookstore.author_debut.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {}
