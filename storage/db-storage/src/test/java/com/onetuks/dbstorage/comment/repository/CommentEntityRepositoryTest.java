package com.onetuks.dbstorage.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.CommentFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.dbstorage.author.repository.AuthorEntityRepository;
import com.onetuks.dbstorage.book.repository.BookEntityRepository;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
import com.onetuks.dbstorage.registration.repository.RegistrationEntityRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class CommentEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired
  private CommentEntityRepository commentEntityRepository;
  @Autowired
  private MemberEntityRepository memberEntityRepository;
  @Autowired
  private AuthorEntityRepository authorEntityRepository;
  @Autowired
  private RegistrationEntityRepository registrationEntityRepository;
  @Autowired
  private BookEntityRepository bookEntityRepository;

  private Member member;
  private Book book;

  @BeforeEach
  void setUp() {
    member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    book =
        bookEntityRepository.create(
            registrationEntityRepository.create(
                RegistrationFixture.create(
                    null,
                    authorEntityRepository.create(
                        AuthorFixture.create(
                            null,
                            memberEntityRepository.create(
                                MemberFixture.create(null, RoleType.AUTHOR)))),
                    false)));
  }

  @Test
  void create() {
    // Given
    Comment comment = CommentFixture.create(null, book, member);

    // When
    Comment result = commentEntityRepository.create(comment);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(comment.title()),
        () -> assertThat(result.content()).isEqualTo(comment.content()));
  }

  @Test
  void read() {
    // Given
    Comment comment = commentEntityRepository.create(CommentFixture.create(null, book, member));

    // When
    Comment result = commentEntityRepository.read(comment.commentId());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(comment.title()),
        () -> assertThat(result.content()).isEqualTo(comment.content()));
  }

  @Test
  void readAllByMember() {
    // Given
    List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    bookEntityRepository.create(
                        registrationEntityRepository.create(
                            RegistrationFixture.create(
                                null,
                                authorEntityRepository.create(
                                    AuthorFixture.create(
                                        null,
                                        memberEntityRepository.create(
                                            MemberFixture.create(null, RoleType.AUTHOR)))),
                                false))))
            .toList();
    Page<Comment> comments =
        new PageImpl<>(
            books.stream()
                .map(
                    book ->
                        commentEntityRepository.create(CommentFixture.create(null, book, member)))
                .toList());

    // When
    Page<Comment> results =
        commentEntityRepository.readAllByMember(member.memberId(), comments.getPageable());

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size());
  }

  @Test
  void readAllByBook() {
    // Given
    List<Member> members =
        IntStream.range(0, 5)
            .mapToObj(i -> memberEntityRepository.create(
                MemberFixture.create(null, RoleType.AUTHOR)))
            .toList();
    Page<Comment> comments =
        new PageImpl<>(
            members.stream()
                .map(member ->
                    commentEntityRepository.create(
                        CommentFixture.create(null, book, member)))
                .toList());

    // When
    Page<Comment> results =
        commentEntityRepository.readAllByBook(book.bookId(), comments.getPageable());

    // Then
    assertThat(results.getTotalElements()).isEqualTo(members.size());
  }

  @Test
  void update() {
    // Given
    Comment comment = commentEntityRepository.create(
        CommentFixture.create(null, book, member));
    Comment updateComment = new Comment(
        comment.commentId(), comment.book(), comment.member(),
        "updated title", "updated content");

    // When
    Comment result = commentEntityRepository.update(updateComment);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(updateComment.title()),
        () -> assertThat(result.content()).isEqualTo(updateComment.content()));
  }

  @Test
  void delete() {
  }
}
