package com.onetuks.dbstorage.comment.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private CommentEntityRepository commentEntityRepository;
  @Autowired private MemberEntityRepository memberEntityRepository;
  @Autowired private AuthorEntityRepository authorEntityRepository;
  @Autowired private RegistrationEntityRepository registrationEntityRepository;
  @Autowired private BookEntityRepository bookEntityRepository;

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
  void read() {}

  @Test
  void readAllByBook() {}

  @Test
  void readAllByMember() {}

  @Test
  void update() {}

  @Test
  void delete() {}
}
