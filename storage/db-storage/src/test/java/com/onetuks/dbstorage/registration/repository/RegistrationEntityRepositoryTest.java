package com.onetuks.dbstorage.registration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.registration.model.Registration;
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

class RegistrationEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private RegistrationEntityRepository registrationEntityRepository;

  @Autowired private MemberEntityRepository memberEntityRepository;
  @Autowired private AuthorEntityRepository authorEntityRepository;

  private Author author;

  @BeforeEach
  void setUp() {
    author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));
  }

  @Test
  void create() {
    // Given
    Registration registration = RegistrationFixture.create(null, author, false);

    // When
    Registration result = registrationEntityRepository.create(registration);

    // Then
    assertThat(result.author().authorId()).isEqualTo(author.authorId());
    assertThat(result.bookConceptualInfo().title())
        .isEqualTo(registration.bookConceptualInfo().title());
  }

  @Test
  void read() {
    // Given
    Registration registration =
        registrationEntityRepository.create(RegistrationFixture.create(null, author, false));

    // When
    Registration result = registrationEntityRepository.read(registration.registrationId());

    // Then
    assertThat(result.author().authorId()).isEqualTo(author.authorId());
    assertThat(result.bookConceptualInfo().title())
        .isEqualTo(registration.bookConceptualInfo().title());
  }

  @Test
  void readByIsbn() {
    // Given
    Registration registration =
        registrationEntityRepository.create(RegistrationFixture.create(null, author, false));

    // When
    Registration result =
        registrationEntityRepository.readByIsbn(registration.bookConceptualInfo().isbn());

    // Then
    assertThat(result.author().authorId()).isEqualTo(author.authorId());
    assertThat(result.bookConceptualInfo().title())
        .isEqualTo(registration.bookConceptualInfo().title());
  }

  @Test
  void readAll() {
    // Given
    List<Registration> registrations =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    registrationEntityRepository.create(
                        RegistrationFixture.create(null, author, false)))
            .toList();

    // When
    Page<Registration> results = registrationEntityRepository.readAll(PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(registrations.size());
  }

  @Test
  void readAll_WithAuthorId() {
    // Given
    List<Registration> registrations =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    registrationEntityRepository.create(
                        RegistrationFixture.create(null, author, false)))
            .toList();

    // When
    Page<Registration> results =
        registrationEntityRepository.readAll(author.authorId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(registrations.size());
  }

  @Test
  void update() {
    // Given
    Registration registration =
        registrationEntityRepository.create(RegistrationFixture.create(null, author, false));
    Registration updatedRegistration = registration.changeApprovalInfo(true, null);

    // When
    Registration result = registrationEntityRepository.update(updatedRegistration);

    // Then
    assertThat(result.author().authorId()).isEqualTo(author.authorId());
    assertThat(result.bookConceptualInfo().title())
        .isEqualTo(registration.bookConceptualInfo().title());
  }

  @Test
  void delete() {
    // Given
    Registration registration =
        registrationEntityRepository.create(RegistrationFixture.create(null, author, false));

    // When
    registrationEntityRepository.delete(registration.registrationId());

    // Then
    assertThatThrownBy(() -> registrationEntityRepository.read(registration.registrationId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
