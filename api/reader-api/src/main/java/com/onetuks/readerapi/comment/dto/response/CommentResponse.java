package com.onetuks.readerapi.comment.dto.response;

import com.onetuks.coredomain.comment.model.Comment;
import org.springframework.data.domain.Page;

public record CommentResponse(
    long commentId, long bookId, long memberId, String title, String content) {

  public static CommentResponse from(Comment comment) {
    return new CommentResponse(
        comment.commentId(),
        comment.book().bookId(),
        comment.member().memberId(),
        comment.title(),
        comment.content());
  }

  public record CommentResponses(Page<CommentResponse> responses) {
    public static CommentResponses from(Page<Comment> comments) {
      return new CommentResponses(comments.map(CommentResponse::from));
    }
  }
}
