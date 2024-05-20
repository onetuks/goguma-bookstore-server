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
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
  @DisplayName("서평을 등록한다.")
  void createCommentTest() {
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

  @Test
  @DisplayName("서평을 조회한다.")
  void readCommentTest() {
    // Given
    Comment comment = CommentFixture.create(null, book, member);

    given(commentRepository.read(comment.commentId())).willReturn(comment);

    // When
    Comment result = commentService.readComment(comment.commentId());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(comment.title()),
        () -> assertThat(result.content()).isEqualTo(comment.content()));
  }

  @Test
  @DisplayName("멤버의 모든 서평을 조회한다.")
  void readAllCommentsOfMemberTest() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    BookFixture.create(
                        createId(),
                        AuthorFixture.create(
                            createId(), MemberFixture.create(createId(), RoleType.AUTHOR))))
            .toList();
    Page<Comment> comments =
        new PageImpl<>(
            books.stream().map(book -> CommentFixture.create(null, book, member)).toList());

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(commentRepository.readAllByMember(member.memberId(), pageable)).willReturn(comments);

    // When
    Page<Comment> results = commentService.readAllCommentsOfMember(member.memberId(), pageable);

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size());
  }
}
