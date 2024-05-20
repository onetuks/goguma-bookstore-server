package com.onetuks.dbstorage.comment.repository;

import com.onetuks.dbstorage.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {}
