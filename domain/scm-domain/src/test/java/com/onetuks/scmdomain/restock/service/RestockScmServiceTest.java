package com.onetuks.scmdomain.restock.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RestockFixture;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class RestockScmServiceTest extends ScmDomainIntegrationTest {

  @Test
  void readRestockBookCount() {
    // Given
    int count = 5;
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Page<Restock> restocks =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i ->
                        RestockFixture.create(
                            createId(),
                            member,
                            BookFixture.create(
                                createId(),
                                AuthorFixture.create(
                                    createId(),
                                    MemberFixture.create(createId(), RoleType.AUTHOR)))))
                .toList());

    given(restockScmRepository.readCount(member.memberId())).willReturn((long) count);

    // When
    long result = restockScmService.readRestockBookCount(member.memberId());

    // Then
    assertThat(result).isEqualTo(restocks.getTotalElements());
  }

  @Test
  void readAllRestockBooks() {
    // Given
    int count = 5;
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Page<Restock> restocks =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i ->
                        RestockFixture.create(
                            createId(),
                            member,
                            BookFixture.create(
                                createId(),
                                AuthorFixture.create(
                                    createId(),
                                    MemberFixture.create(createId(), RoleType.AUTHOR)))))
                .toList());

    given(restockScmRepository.readAllByAuthor(member.memberId(), PageRequest.of(0, 10)))
        .willReturn(restocks);

    // When
    Page<Restock> results =
        restockScmService.readAllRestockBooks(member.memberId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(count);
  }
}
