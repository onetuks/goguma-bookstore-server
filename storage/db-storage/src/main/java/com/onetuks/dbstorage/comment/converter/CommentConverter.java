package com.onetuks.dbstorage.comment.converter;

import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.comment.entity.CommentEntity;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

  private final MemberConverter memberConverter;
  private final BookConverter bookConverter;

  public CommentConverter(MemberConverter memberConverter, BookConverter bookConverter) {
    this.memberConverter = memberConverter;
    this.bookConverter = bookConverter;
  }

  public CommentEntity toEntity(Comment comment) {
    return new CommentEntity(
        comment.commentId(),
        bookConverter.toEntity(comment.book()),
        memberConverter.toEntity(comment.member()),
        comment.title(),
        comment.content());
  }

  public Comment toDomain(CommentEntity commentEntity) {
    return new Comment(
        commentEntity.getCommentId(),
        bookConverter.toDomain(commentEntity.getBookEntity()),
        memberConverter.toDomain(commentEntity.getMemberEntity()),
        commentEntity.getTitle(),
        commentEntity.getContent());
  }
}
