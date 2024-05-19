package com.onetuks.dbstorage.subscribe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.SubscribeFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.subscribe.model.Subscribe;
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

class SubscribeEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private SubscribeEntityRepository subscribeEntityRepository;

  @Autowired private MemberEntityRepository memberEntityRepository;
  @Autowired private AuthorEntityRepository authorEntityRepository;

  private Member member;
  private Author author;

  @BeforeEach
  void setUp() {
    member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    author =
        authorEntityRepository.create(
            AuthorFixture.create(
                null, memberEntityRepository.create(MemberFixture.create(null, RoleType.AUTHOR))));
  }

  @Test
  void create() {
    // Given
    Subscribe subscribe = SubscribeFixture.create(null, member, author);

    // When
    Subscribe result = subscribeEntityRepository.create(subscribe);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.author().authorId()).isEqualTo(author.authorId()));
  }

  @Test
  void read() {
    // Given
    Subscribe subscribe =
        subscribeEntityRepository.create(SubscribeFixture.create(null, member, author));

    // When
    Subscribe result = subscribeEntityRepository.read(subscribe.subscribeId());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.author().authorId()).isEqualTo(author.authorId()));
  }

  @Test
  void readExistence() {
    // Given
    subscribeEntityRepository.create(SubscribeFixture.create(null, member, author));

    // When
    boolean result = subscribeEntityRepository.readExistence(member.memberId(), author.authorId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void readAll() {
    // Given
    List<Subscribe> subscribes =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    authorEntityRepository.create(
                        AuthorFixture.create(
                            null,
                            memberEntityRepository.create(
                                MemberFixture.create(null, RoleType.AUTHOR)))))
            .map(
                author ->
                    subscribeEntityRepository.create(SubscribeFixture.create(null, member, author)))
            .toList();

    // When
    Page<Subscribe> results =
        subscribeEntityRepository.readAll(member.memberId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(subscribes.size());
  }

  @Test
  void delete() {
    // Given
    Subscribe subscribe =
        subscribeEntityRepository.create(SubscribeFixture.create(null, member, author));

    // When
    subscribeEntityRepository.delete(subscribe.subscribeId());

    // Then
    assertThatThrownBy(() -> subscribeEntityRepository.read(subscribe.subscribeId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
