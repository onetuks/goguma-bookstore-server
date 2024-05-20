package com.onetuks.readerapi.comment.dto.response;

import com.onetuks.coredomain.comment.model.Comment;

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
}
