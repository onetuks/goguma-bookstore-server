package com.onetuks.modulereader.favorite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;

import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.favorite.repository.FavoriteJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.BookFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.favorite.service.dto.result.FavoriteGetResult;
import com.onetuks.modulereader.favorite.service.dto.result.FavoritePostResult;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class FavoriteServiceTest extends ReaderIntegrationTest {

  @Autowired
  private FavoriteService favoriteService;
  @Autowired
  private MemberJpaRepository memberJpaRepository;
  @Autowired
  private AuthorJpaRepository authorJpaRepository;
  @Autowired
  private BookJpaRepository bookJpaRepository;
  @Autowired
  private FavoriteJpaRepository favoriteJpaRepository;

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

    favoriteService.createFavorite(member.getMemberId(), book.getBookId());
  }

  @Test
  @DisplayName("즐겨찾기 한 적이 없는 도서를 즐겨찾기하면 성공한다. 도서의 즐겨찾기 카운트가 1 증가한다.")
  void createFavoriteTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    Book book =
        bookJpaRepository.save(
            BookFixture.create(
                authorJpaRepository.save(
                    AuthorFixture.create(
                        memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))))));
    long prevFavoriteCount = book.getBookStatics().getFavoriteCount();

    // When
    FavoritePostResult result =
        favoriteService.createFavorite(member.getMemberId(), book.getBookId());

    // Then
    Long postFavoriteCount = book.getBookStatics().getFavoriteCount();

    assertThat(postFavoriteCount).isEqualTo(prevFavoriteCount + 1);
    assertThat(result.favoriteId()).isPositive();
  }

  @Test
  @DisplayName("이미 즐겨찾기한 도서를 다시 즐겨찾기하면 예외를 던진다. 도서의 즐겨찾기 카운트는 변하지 않는다.")
  void createFavorite_DuplicateFavorite_ExceptionTest() {
    // Given & When
    Exception exception =
        catchException(
            () -> favoriteService.createFavorite(member.getMemberId(), book.getBookId()));

    // Then
    assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("즐겨찾기한 도서를 해제한다.")
  void deleteFavoriteTest() {
    // Given
    long prevFavoriteCount = book.getBookStatics().getFavoriteCount();

    // When
    favoriteService.deleteFavorite(
        member.getMemberId(), favoriteJpaRepository.findAll().get(0).getFavoriteId());

    // Then
    Long postFavoriteCount = book.getBookStatics().getFavoriteCount();

    assertThat(postFavoriteCount).isEqualTo(prevFavoriteCount - 1);
  }

  @Test
  @DisplayName("권한 없는 유저가 즐겨찾기 도서를 해제하려고 하면 예외를 던진다.")
  void deleteFavorite_AccessDenied_ExceptionTest() {
    // Given
    long notExistsMemberId = 123_141_145_123L;

    // When & Then
    assertThatThrownBy(
        () ->
            favoriteService.deleteFavorite(
                notExistsMemberId, favoriteJpaRepository.findAll().get(0).getFavoriteId()))
        .isInstanceOf(ApiAccessDeniedException.class);
  }

  @Test
  @DisplayName("유저가 즐겨찾기 하지 않은 도서를 이용해 즐겨찾기를 조회하면 false 를 반환한다.")
  void readFavoriteExistence_NotFavoritedTest() {
    // Given
    Member userMember = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    Book notFavoritedBook =
        bookJpaRepository.save(
            BookFixture.create(
                authorJpaRepository.save(
                    AuthorFixture.create(
                        memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))))));

    // When
    boolean result =
        favoriteService
            .readFavoriteExistence(userMember.getMemberId(), notFavoritedBook.getBookId())
            .isFavorited();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("유저가 즐겨찾기한 도서를 이용해 즐겨찾기를 조회하면 true 를 반환한다.")
  void readFavoriteExistence_FavoritedTest() {
    // Given & When
    boolean result =
        favoriteService.readFavoriteExistence(member.getMemberId(), book.getBookId()).isFavorited();

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("유저의 모든 즐겨찾기를 조회한다.")
  void readFavoritesOfMemberTest() {
    // Given
    Member authorMember = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author = authorJpaRepository.save(AuthorFixture.create(authorMember));
    List<Book> books =
        IntStream.range(0, 3)
            .mapToObj(i -> bookJpaRepository.save(BookFixture.create(author)))
            .toList();
    books.forEach(
        book -> favoriteService.createFavorite(authorMember.getMemberId(), book.getBookId()));

    // When
    Page<FavoriteGetResult> results =
        favoriteService.readFavoritesOfMember(authorMember.getMemberId(), PageRequest.of(0, 10));

    // Then
    assertThat(results).isNotEmpty().hasSize(books.size());
  }
}
