package com.onetuks.readerdomain.comment.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.CommentFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentServiceTest extends ReaderDomainIntegrationTest {

  private Member member;
  private Book book;

  @BeforeEach
  void setUp() {
    member = MemberFixture.create(createId(), RoleType.USER);
    book =
        BookFixture.create(
            createId(),
            AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR)));
  }

  @Test
  void createComment() {
    // Given
    Comment comment = CommentFixture.create(null, book, member);

    given(bookRepository.read(book.bookId())).willReturn(book);
    given(memberRepository.read(member.memberId())).willReturn(member);
    given(commentRepository.create(any(Comment.class))).willReturn(comment);

    // When
    Comment result =
        commentService.createComment(
            member.memberId(), book.bookId(), comment.title(), comment.content());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(comment.title()),
        () -> assertThat(result.content()).isEqualTo(comment.content()));
  }
}
