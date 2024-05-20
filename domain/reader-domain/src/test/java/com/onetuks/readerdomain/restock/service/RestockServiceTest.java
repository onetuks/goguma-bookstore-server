package com.onetuks.readerdomain.restock.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RestockFixture;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class RestockServiceTest extends ReaderDomainIntegrationTest {

  @Test
  @DisplayName("도서 재입고를 신청한다.")
  void createRestockTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Book book =
        BookFixture.create(
            createId(),
            AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR)));
    Restock restock = RestockFixture.create(null, member, book);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(restockRepository.create(any())).willReturn(restock);

    // When
    Restock result = restockService.createRestock(member.memberId(), book.bookId());

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.isAlarmPermitted()).isEqualTo(member.isAlarmPermitted()));
  }

  @Test
  @DisplayName("독자의 모든 재입고 신청을 조회한다.")
  void readAllRestocksTest() {
    // Given
    int count = 5;
    Pageable pageable = PageRequest.of(0, 10);
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

    given(restockRepository.readAll(member.memberId(), pageable)).willReturn(restocks);

    // When
    Page<Restock> results = restockService.readAllRestocks(member.memberId(), pageable);

    // Then
    assertThat(results.getTotalElements()).isEqualTo(count);
  }

  @Test
  @DisplayName("재입고 알림 설정을 변경한다.")
  void updateRestockAlarmTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Restock restock =
        RestockFixture.create(
            createId(),
            member,
            BookFixture.create(
                createId(),
                AuthorFixture.create(
                    createId(), MemberFixture.create(createId(), RoleType.AUTHOR))));

    given(restockRepository.read(restock.restockId())).willReturn(restock);
    given(restockRepository.update(any())).willReturn(restock.changeAlarmPermitted(false));

    // When
    Restock result =
        restockService.updateRestockAlarm(member.memberId(), restock.restockId(), false);

    // Then
    assertAll(
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(restock.book().bookId()),
        () -> assertThat(result.isAlarmPermitted()).isFalse());
  }

  @Test
  @DisplayName("도서 재입고를 취소한다.")
  void deleteRestockTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Restock restock =
        RestockFixture.create(
            createId(),
            member,
            BookFixture.create(
                createId(),
                AuthorFixture.create(
                    createId(), MemberFixture.create(createId(), RoleType.AUTHOR))));

    given(restockRepository.read(restock.restockId())).willReturn(restock);

    // When
    restockService.deleteRestock(member.memberId(), restock.restockId());

    // Then
    assertThatThrownBy(() -> restockService.deleteRestock(createId(), restock.restockId()))
        .isInstanceOf(ApiAccessDeniedException.class);
  }
}
