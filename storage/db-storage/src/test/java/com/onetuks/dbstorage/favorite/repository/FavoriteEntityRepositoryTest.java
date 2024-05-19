package com.onetuks.dbstorage.favorite.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.FavoriteFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.dbstorage.author.repository.AuthorEntityRepository;
import com.onetuks.dbstorage.book.repository.BookEntityRepository;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class FavoriteEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private FavoriteEntityRepository favoriteEntityRepository;

  @Autowired private MemberEntityRepository memberEntityRepository;
  @Autowired private AuthorEntityRepository authorEntityRepository;
  @Autowired private BookEntityRepository bookEntityRepository;

  private Member userMember;
  private Author author;
  private Book book;

  @BeforeEach
  void setUp() {
    userMember = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));
    book = bookEntityRepository.create(RegistrationFixture.create(null, author, true));
  }

  @Test
  void create() {
    // Given
    final Favorite favorite = FavoriteFixture.create(null, userMember, book);

    // When
    final Favorite result = favoriteEntityRepository.create(favorite);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(userMember.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()));
  }

  @Test
  void read() {
    // Given
    final Favorite favorite =
        favoriteEntityRepository.create(FavoriteFixture.create(null, userMember, book));

    // When
    Favorite result = favoriteEntityRepository.read(favorite.favoriteId());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(userMember.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()));
  }

  @Test
  void readExistence() {
    // Given
    favoriteEntityRepository.create(FavoriteFixture.create(null, userMember, book));

    // When
    boolean result = favoriteEntityRepository.readExistence(userMember.memberId(), book.bookId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void readAll() {
    // Given
    favoriteEntityRepository.create(FavoriteFixture.create(null, userMember, book));

    // When
    Page<Favorite> results =
        favoriteEntityRepository.readAll(userMember.memberId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(1);
  }

  @Test
  void delete() {
    // Given
    final Favorite favorite =
        favoriteEntityRepository.create(FavoriteFixture.create(null, userMember, book));

    // When
    favoriteEntityRepository.delete(favorite.favoriteId());

    // Then
    assertThatThrownBy(() -> favoriteEntityRepository.read(favorite.favoriteId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
