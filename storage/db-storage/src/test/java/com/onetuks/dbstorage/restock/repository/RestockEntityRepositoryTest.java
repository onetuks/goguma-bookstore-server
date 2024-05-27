package com.onetuks.dbstorage.restock.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.RestockFixture;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.dbstorage.author.repository.AuthorEntityRepository;
import com.onetuks.dbstorage.book.repository.BookEntityRepository;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
import com.onetuks.dbstorage.registration.repository.RegistrationEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class RestockEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private RestockEntityRepository restockEntityRepository;

  @Autowired private MemberEntityRepository memberEntityRepository;
  @Autowired private AuthorEntityRepository authorEntityRepository;
  @Autowired private BookEntityRepository bookEntityRepository;
  @Autowired private RegistrationEntityRepository registrationEntityRepository;

  private Member userMember;
  private Book book;

  @BeforeEach
  void setUp() {
    userMember = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
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
                    true)));
  }

  @Test
  void create() {
    // Given
    Restock restock = RestockFixture.create(null, userMember, book);

    // When
    Restock result = restockEntityRepository.create(restock);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(userMember.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()));
  }

  @Test
  void read() {
    // Given
    Restock restock = restockEntityRepository.create(RestockFixture.create(null, userMember, book));

    // When
    Restock result = restockEntityRepository.read(restock.restockId());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(userMember.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()));
  }

  @Test
  void readAll() {
    // Given
    restockEntityRepository.create(RestockFixture.create(null, userMember, book));

    // When
    Page<Restock> results =
        restockEntityRepository.readAll(userMember.memberId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isOne();
  }

  @Test
  void update() {
    // Given
    Restock restock = restockEntityRepository.create(RestockFixture.create(null, userMember, book));
    Restock updatedRestock = restock.changeAlarmPermitted(false);

    // When
    Restock result = restockEntityRepository.update(updatedRestock);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(userMember.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.isAlarmPermitted()).isFalse());
  }

  @Test
  void delete() {
    // Given
    Restock restock = restockEntityRepository.create(RestockFixture.create(null, userMember, book));

    // When
    restockEntityRepository.delete(restock.restockId());

    // Then
    assertThatThrownBy(() -> restockEntityRepository.read(restock.restockId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
