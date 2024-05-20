package com.onetuks.dbstorage.comment.repository;

import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.coredomain.comment.repository.CommentRepository;
import com.onetuks.dbstorage.comment.converter.CommentConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CommentEntityRepository implements CommentRepository {

  private final CommentJpaRepository repository;
  private final CommentConverter converter;

  public CommentEntityRepository(CommentJpaRepository repository, CommentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Comment create(Comment comment) {
    return converter.toDomain(repository.save(converter.toEntity(comment)));
  }

  @Override
  public Comment read(long commentId) {
    return null;
  }

  @Override
  public Page<Comment> readAllByBook(long bookId, Pageable pageable) {
    return null;
  }

  @Override
  public Page<Comment> readAllByMember(long memberId, Pageable pageable) {
    return null;
  }

  @Override
  public Comment update(Comment comment) {
    return null;
  }

  @Override
  public void delete(long commentId) {}
}
