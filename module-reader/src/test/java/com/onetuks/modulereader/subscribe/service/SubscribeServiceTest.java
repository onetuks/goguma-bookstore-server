package com.onetuks.modulereader.subscribe.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.subscribe.repository.SubscribeJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.subscribe.service.dto.param.SubscribePostParam;
import com.onetuks.modulereader.subscribe.service.dto.result.SubscribePostResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class SubscribeServiceTest extends ReaderIntegrationTest {

  @Autowired private SubscribeService subscribeService;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private SubscribeJpaRepository subscribeJpaRepository;

  private Member member;
  private Author author;

  @BeforeEach
  void setUp() {
    member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    author =
        authorJpaRepository.save(
            AuthorFixture.create(memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))));
  }

  @Test
  @DisplayName("중복되지 않은 구독 정보라면 해당 작가에 대한 구독수를 늘리고 구독 성공한다..")
  void createSubscribeTest() {
    // Given
    SubscribePostParam param = new SubscribePostParam(author.getAuthorId());
    Long previousSubscribeCount = author.getAuthorStatics().getSubscribeCount();

    // When
    SubscribePostResult result = subscribeService.createSubscribe(member.getMemberId(), param);

    // Then
    Long postSubscribeCount = author.getAuthorStatics().getSubscribeCount();

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount + 1);
    assertAll(
        () -> assertThat(result.subscribeId()).isPositive(),
        () -> assertThat(result.memberId()).isEqualTo(member.getMemberId()),
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()));
  }

  @Test
  @DisplayName("중복된 구독 신청은 예외를 던지며 작가의 구독수는 증가하지 않는다.")
  void createSubscribe_duplicated_Exception() {
    // Given
    SubscribePostParam param = new SubscribePostParam(author.getAuthorId());
    subscribeService.createSubscribe(member.getMemberId(), param);

    // When & Then
    assertThrows(
        DataIntegrityViolationException.class,
        () -> subscribeService.createSubscribe(member.getMemberId(), param));
  }

  @Test
  @DisplayName("존재하는 구독 정보를 제거하면 성공하며 해당 작가의 구독수가 감소한다.")
  void deleteSubcribeTest() {
    // Given
    SubscribePostParam param = new SubscribePostParam(author.getAuthorId());
    SubscribePostResult postResult = subscribeService.createSubscribe(member.getMemberId(), param);
    long previousSubscribeCount = author.getAuthorStatics().getSubscribeCount();

    // When
    subscribeService.deleteSubcribe(member.getMemberId(), postResult.subscribeId());

    // Then
    long postSubscribeCount = author.getAuthorStatics().getSubscribeCount();
    boolean result = subscribeJpaRepository.existsById(postResult.subscribeId());

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount - 1);
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 구독 정보를 제거하면 예외를 던진다. 구독자수는 감소하지 않는다.")
  void deleteSubsribe_NotExists_Exception() {
    // Given
    long nonExistsSubscribeId = 11_231_231L;
    long previousSubscribeCount = author.getAuthorStatics().getSubscribeCount();

    // When & Then
    assertThatThrownBy(
            () -> {
              subscribeService.deleteSubcribe(member.getMemberId(), nonExistsSubscribeId);
              subscribeJpaRepository.flush();
              authorJpaRepository.flush();
            })
        .isInstanceOf(IllegalArgumentException.class);

    long postSubscribeCount = authorJpaRepository.findById(author.getAuthorId())
        .orElseThrow().getAuthorStatics().getSubscribeCount();

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount);
  }
}
