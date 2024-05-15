package com.onetuks.readerdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.FavoriteFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coredomain.favorite.repository.FavoriteRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import com.onetuks.readerdomain.favorite.service.FavoriteService;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class FavoriteServiceTest extends ReaderDomainIntegrationTest {

  @Autowired private FavoriteService favoriteService;

  @MockBean private FavoriteRepository favoriteRepository;
  @MockBean private MemberRepository memberRepository;
  @MockBean private BookRepository bookRepository;

  @Test
  @DisplayName("즐겨찾기 한 적이 없는 도서를 즐겨찾기하면 성공한다. 도서의 즐겨찾기 카운트가 1 증가한다.")
  void createFavoriteTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    Book book = BookFixture.create(createId(), author);
    Favorite favorite = FavoriteFixture.create(createId(), member, book);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(bookRepository.read(book.bookId())).willReturn(book);
    given(favoriteRepository.create(any())).willReturn(favorite);

    // When
    Favorite result = favoriteService.createFavorite(member.memberId(), book.bookId());

    // Then
    assertThat(result.book().bookStatics().favoriteCount()).isOne();
  }

  @Test
  @DisplayName("유저가 해당 도서에 대해 즐겨찾기 했는지를 조회한다.")
  void readFavoriteExistenceTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    Book book = BookFixture.create(createId(), author);

    given(favoriteRepository.readExistence(member.memberId(), book.bookId())).willReturn(true);

    // When
    boolean result = favoriteService.readFavoriteExistence(member.memberId(), book.bookId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("유저의 모든 즐겨찾기를 조회한다.")
  void readAllFavoriteTest() {
    // Given
    int count = 5;
    Member member = MemberFixture.create(createId(), RoleType.USER);
    List<Book> books =
        IntStream.range(0, count)
            .mapToObj(
                i ->
                    BookFixture.create(
                        createId(),
                        AuthorFixture.create(
                            createId(), MemberFixture.create(createId(), RoleType.AUTHOR))))
            .toList();
    List<Favorite> favorites =
        books.stream().map(book -> FavoriteFixture.create(createId(), member, book)).toList();

    given(favoriteRepository.readAll(member.memberId())).willReturn(favorites);

    // When
    List<Favorite> results = favoriteService.readAllFavorites(member.memberId());

    // Then
    assertThat(results).hasSize(count);
  }

  @Test
  @DisplayName("즐겨찾기한 도서를 해제한다.")
  void deleteFavoriteTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Favorite favorite =
        FavoriteFixture.create(
            createId(),
            member,
            BookFixture.create(
                createId(),
                AuthorFixture.create(
                    createId(), MemberFixture.create(createId(), RoleType.AUTHOR))));

    given(favoriteRepository.read(favorite.favoriteId())).willReturn(favorite);

    // When
    favoriteService.deleteFavorite(member.memberId(), favorite.favoriteId());

    // Then
    verify(favoriteRepository, times(1)).delete(favorite.favoriteId());
  }

  @Test
  @DisplayName("권한 없는 유저가 즐겨찾기 도서를 해제하려고 하면 예외를 던진다.")
  void deleteFavorite_AccessDenied_ExceptionTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Favorite favorite =
        FavoriteFixture.create(
            createId(),
            MemberFixture.create(createId(), RoleType.USER),
            BookFixture.create(
                createId(),
                AuthorFixture.create(
                    createId(), MemberFixture.create(createId(), RoleType.AUTHOR))));

    given(favoriteRepository.read(favorite.favoriteId())).willReturn(favorite);

    // When
    assertThatThrownBy(
            () -> favoriteService.deleteFavorite(member.memberId(), favorite.favoriteId()))
        .isInstanceOf(ApiAccessDeniedException.class);

    // Then
    verify(favoriteRepository, times(0)).delete(favorite.favoriteId());
  }
}
