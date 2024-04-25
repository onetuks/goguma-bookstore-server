package com.onetuks.goguma_bookstore.favorite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoritePostResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.BookFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class FavoriteServiceTest extends IntegrationTest {

  @Autowired private FavoriteService favoriteService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

  private Member member;
  private Book book;

  @BeforeEach
  void setUp() {
    member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    book =
        bookJpaRepository.save(
            BookFixture.create(
                authorJpaRepository.save(
                    AuthorFixture.create(
                        memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))))));
  }

  @Test
  @DisplayName("즐겨찾기 한 적이 없는 도서를 즐겨찾기하면 성공한다.")
  void createFavoriteTest() {
    // Given & When
    FavoritePostResult result =
        favoriteService.createFavorite(member.getMemberId(), book.getBookId());

    // Then
    assertThat(result.favoriteId()).isPositive();
  }

  @Test
  @DisplayName("이미 즐겨찾기한 도서를 다시 즐겨찾기하면 예외를 던진다.")
  void createFavorite_DuplicateFavorite_ExceptionTest() {
    // Given
    favoriteService.createFavorite(member.getMemberId(), book.getBookId());

    // When & Then
    assertThrows(
        DataIntegrityViolationException.class,
        () -> favoriteService.createFavorite(member.getMemberId(), book.getBookId()));
  }
}
