package com.onetuks.dbstorage.book.repository;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.enums.book.PageOrder;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.dbstorage.author.repository.AuthorEntityRepository;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class BookEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private BookEntityRepository bookEntityRepository;

  @Autowired private MemberEntityRepository memberEntityRepository;
  @Autowired private AuthorEntityRepository authorEntityRepository;

  private Registration registration;

  @BeforeEach
  void setUp() {
    registration =
        RegistrationFixture.create(
            createId(),
            authorEntityRepository.create(
                AuthorFixture.create(
                    null,
                    memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR)))),
            true);
  }

  @Test
  void create() {
    // Given & When
    final Book result = bookEntityRepository.create(registration);

    // Then
    assertThat(result.bookId()).isPositive();
    assertThat(result.author().authorStatics().bookCount()).isOne();
  }

  @Test
  void read() {
    // Given
    final Book book = bookEntityRepository.create(registration);

    // When
    final Book result = bookEntityRepository.read(book.bookId());

    // Then
    assertThat(book.bookConceptualInfo().title())
        .isEqualTo(registration.bookConceptualInfo().title());
    //    assertThat(book.bookStatics().viewCount()).isOne();
  }

  @Test
  void readAll() {
    // Given
    final Author author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));
    final List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    bookEntityRepository.create(
                        RegistrationFixture.create(createId(), author, true)))
            .toList();

    // When
    final Page<Book> results =
        bookEntityRepository.readAll(author.authorId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size());
  }

  @Test
  void readAll_WithPage() {
    // Given
    final Author author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));
    final List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    bookEntityRepository.create(
                        RegistrationFixture.create(createId(), author, true)))
            .toList();

    // When
    final Page<Book> results =
        bookEntityRepository.readAll(
            null, null, null, false, false, PageOrder.DATE, PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size());
  }

  @Test
  void update() {
    // Given
    final Book book = bookEntityRepository.create(registration);
    final Book updatedBook = book.changeStockCount(12);

    // When
    final Book result = bookEntityRepository.update(updatedBook);

    // Then
    assertAll(
        () ->
            assertThat(result.bookConceptualInfo().title())
                .isEqualTo(updatedBook.bookConceptualInfo().title()),
        () ->
            assertThat(result.bookPriceInfo().stockCount())
                .isEqualTo(updatedBook.bookPriceInfo().stockCount()));
  }

  @Test
  void delete() {
    // Given
    final Book book = bookEntityRepository.create(registration);

    // When
    bookEntityRepository.delete(book.bookId());

    // Then
    assertThatThrownBy(() -> bookEntityRepository.read(book.bookId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
