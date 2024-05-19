package com.onetuks.dbstorage.author.repository;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.dbstorage.member.repository.MemberEntityRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class AuthorEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private AuthorEntityRepository authorEntityRepository;

  @Autowired private MemberEntityRepository memberEntityRepository;

  @Test
  void create() {
    // Given
    Author author =
        AuthorFixture.create(
            null, memberEntityRepository.create(MemberFixture.create(createId(), RoleType.AUTHOR)));

    // When
    final Author result = authorEntityRepository.create(author);

    // Then
    assertThat(result.member().authInfo().roles()).contains(RoleType.AUTHOR);
  }

  @Test
  void read() {
    // Given
    Author author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    final Author result = authorEntityRepository.read(author.authorId());

    // Then
    assertThat(result.nickname().nicknameValue()).isEqualTo(author.nickname().nicknameValue());
  }

  @Test
  void readByMember() {
    // Given
    Member member =
        memberEntityRepository.create(MemberFixture.create(createId(), RoleType.AUTHOR));
    Author author = authorEntityRepository.create(AuthorFixture.create(null, member));

    // When
    final Author result = authorEntityRepository.readByMember(member.memberId());

    // Then
    assertThat(result.member()).isEqualTo(member);
    assertThat(result.authorId()).isEqualTo(author.authorId());
  }

  @Test
  void readAll() {
    // Given
    List<Author> authors =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    authorEntityRepository.create(
                        AuthorFixture.create(
                            null,
                            memberEntityRepository.create(
                                MemberFixture.create(createId(), RoleType.AUTHOR)))))
            .toList();

    // When
    final List<Author> results = authorEntityRepository.readAll();

    // Then
    assertThat(results).hasSize(authors.size());
  }

  @Test
  void readAll_WithPageable() {
    // Given
    List<Author> authors =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    authorEntityRepository.create(
                        AuthorFixture.create(
                            null,
                            memberEntityRepository.create(
                                MemberFixture.create(createId(), RoleType.AUTHOR)))))
            .toList();

    // When
    final Page<Author> results = authorEntityRepository.readAll(PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(authors.size());
  }

  @Test
  void readAllEnrollmentPassed() {
    // Given
    List<Author> authors =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    authorEntityRepository.create(
                        AuthorFixture.create(
                            null,
                            memberEntityRepository.create(
                                MemberFixture.create(createId(), RoleType.AUTHOR)))))
            .toList();

    // When
    final Page<Author> results =
        authorEntityRepository.readAllEnrollmentPassed(PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(authors.size());
  }

  @Test
  void update() {
    // Given
    Author author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));
    Author updatedAuthor =
        author.changeAuthorProfile(
            "profileImgUri", "new nickname",
            "new introduction", "new InstagramUrl");

    // When
    final Author result = authorEntityRepository.update(updatedAuthor);

    // Then
    assertAll(
        () -> assertThat(result.profileImgFilePath()).isEqualTo(updatedAuthor.profileImgFilePath()),
        () -> assertThat(result.introduction()).isEqualTo(updatedAuthor.introduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(updatedAuthor.instagramUrl()));
  }

  @Test
  void delete() {
    // Given
    Author author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));

    // When
    authorEntityRepository.delete(author.authorId());

    // Then
    assertThatThrownBy(() -> authorEntityRepository.read(author.authorId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
