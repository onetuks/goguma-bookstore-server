package com.onetuks.readerdomain.comment.service;

import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.coredomain.comment.repository.CommentRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;

  public CommentService(
      CommentRepository commentRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository) {
    this.commentRepository = commentRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public Comment createComment(long memberId, long bookId, String title, String content) {
    return commentRepository.create(
        new Comment(
            null, bookRepository.read(bookId), memberRepository.read(memberId), title, content));
  }

  @Transactional(readOnly = true)
  public Comment readComment(long commentId) {
    return commentRepository.read(commentId);
  }

  @Transactional(readOnly = true)
  public Page<Comment> readAllCommentsOfMember(long memberId, Pageable pageable) {
    return commentRepository.readAllByMember(memberId, pageable);
  }

  public Page<Comment> readAllCommentsOfBook(long bookId, Pageable pageable) {
    return commentRepository.readAllByBook(bookId, pageable);
  }

  public Comment updateComment(long memberId, long commentId, String title, String content) {
    Comment comment = commentRepository.read(commentId);

    if (comment.member().memberId() != memberId) {
      throw new ApiAccessDeniedException("해당 서평에 대한 권한이 없는 멤버입니다.");
    }

    return commentRepository.update(
        new Comment(commentId, comment.book(), comment.member(), title, content));
  }
}
