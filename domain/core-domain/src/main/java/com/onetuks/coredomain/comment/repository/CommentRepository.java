package com.onetuks.coredomain.comment.repository;

import com.onetuks.coredomain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {

  Comment create(Comment comment);

  Comment read(long commentId);

  Page<Comment> readAllByMember(long memberId, Pageable pageable);

  Page<Comment> readAllByBook(long bookId, Pageable pageable);

  Comment update(Comment comment);

  void delete(long commentId);
}
