package com.onetuks.modulereader.subscribe.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.subscribe.repository.SubscribeJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.subscribe.service.dto.param.SubscribePostParam;
import com.onetuks.modulereader.subscribe.service.dto.result.SubscribeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class SubscribeEntityServiceTest extends ReaderIntegrationTest {

  @Autowired private SubscribeService subscribeService;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private SubscribeJpaRepository subscribeJpaRepository;

  private MemberEntity memberEntity;
  private AuthorEntity authorEntity;

  @BeforeEach
  void setUp() {
    memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    authorEntity =
        authorJpaRepository.save(
            AuthorFixture.create(memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))));
  }

  @Test
  @DisplayName("중복되지 않은 구독 정보라면 해당 작가에 대한 구독수를 늘리고 구독 성공한다..")
  void createSubscribeTest() {
    // Given
    SubscribePostParam param = new SubscribePostParam(authorEntity.getAuthorId());
    Long previousSubscribeCount = authorEntity.getAuthorStaticsEntity().getSubscribeCount();

    // When
    SubscribeResult result = subscribeService.createSubscribe(memberEntity.getMemberId(), param);

    // Then
    Long postSubscribeCount = authorEntity.getAuthorStaticsEntity().getSubscribeCount();

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount + 1);
    assertAll(
        () -> assertThat(result.subscribeId()).isPositive(),
        () -> assertThat(result.memberId()).isEqualTo(memberEntity.getMemberId()),
        () -> assertThat(result.authorId()).isEqualTo(authorEntity.getAuthorId()));
  }

  @Test
  @DisplayName("중복된 구독 신청은 예외를 던지며 작가의 구독수는 증가하지 않는다.")
  void createSubscribe_duplicated_Exception() {
    // Given
    SubscribePostParam param = new SubscribePostParam(authorEntity.getAuthorId());
    subscribeService.createSubscribe(memberEntity.getMemberId(), param);

    // When & Then
    assertThrows(
        DataIntegrityViolationException.class,
        () -> subscribeService.createSubscribe(memberEntity.getMemberId(), param));
  }

  @Test
  @DisplayName("존재하는 구독 정보를 제거하면 성공하며 해당 작가의 구독수가 감소한다.")
  void deleteSubcribeTest() {
    // Given
    SubscribePostParam param = new SubscribePostParam(authorEntity.getAuthorId());
    SubscribeResult postResult = subscribeService.createSubscribe(memberEntity.getMemberId(), param);
    long previousSubscribeCount = authorEntity.getAuthorStaticsEntity().getSubscribeCount();

    // When
    subscribeService.deleteSubcribe(memberEntity.getMemberId(), postResult.subscribeId());

    // Then
    long postSubscribeCount = authorEntity.getAuthorStaticsEntity().getSubscribeCount();
    boolean result = subscribeJpaRepository.existsById(postResult.subscribeId());

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount - 1);
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 구독 정보를 제거하면 예외를 던진다. 구독자수는 감소하지 않는다.")
  void deleteSubsribe_NotExists_Exception() {
    // Given
    long nonExistsSubscribeId = 11_231_231L;
    long previousSubscribeCount = authorEntity.getAuthorStaticsEntity().getSubscribeCount();

    // When & Then
    assertThatThrownBy(
            () -> {
              subscribeService.deleteSubcribe(memberEntity.getMemberId(), nonExistsSubscribeId);
              subscribeJpaRepository.flush();
              authorJpaRepository.flush();
            })
        .isInstanceOf(IllegalArgumentException.class);

    long postSubscribeCount =
        authorJpaRepository
            .findById(authorEntity.getAuthorId())
            .orElseThrow()
            .getAuthorStaticsEntity()
            .getSubscribeCount();

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount);
  }

  @Test
  @DisplayName("다른 유저가 구독 정보를 제거하려고 하면 예외를 던진다. 구독자수는 감소하지 않는다.")
  void deleteSubsribe_AccessDenied_Exception() {
    // Given
    long notAuthorizedMemberId = 11_231_231L;
    SubscribePostParam param = new SubscribePostParam(authorEntity.getAuthorId());
    SubscribeResult postResult = subscribeService.createSubscribe(memberEntity.getMemberId(), param);
    long previousSubscribeCount = authorEntity.getAuthorStaticsEntity().getSubscribeCount();

    // When & Then
    assertThatThrownBy(
            () -> subscribeService.deleteSubcribe(notAuthorizedMemberId, postResult.subscribeId()))
        .isInstanceOf(ApiAccessDeniedException.class);

    long postSubscribeCount =
        authorJpaRepository
            .findById(authorEntity.getAuthorId())
            .orElseThrow()
            .getAuthorStaticsEntity()
            .getSubscribeCount();

    assertThat(postSubscribeCount).isEqualTo(previousSubscribeCount);
  }

  @Test
  @DisplayName("해당 작가에 대해서 구독이 되어있는지 여부를 조회한다.")
  void readIsSubscribedAuthorTest() {
    // Given
    SubscribePostParam param = new SubscribePostParam(authorEntity.getAuthorId());
    subscribeService.createSubscribe(memberEntity.getMemberId(), param);

    // When
    boolean result =
        subscribeService.readIsSubscribedAuthor(memberEntity.getMemberId(), authorEntity.getAuthorId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("유저의 모든 구독 정보를 조회한다.")
  void readAllSubscribesTest() {
    // Given
    SubscribePostParam param = new SubscribePostParam(authorEntity.getAuthorId());
    subscribeService.createSubscribe(memberEntity.getMemberId(), param);

    // When
    Page<SubscribeResult> result =
        subscribeService.readAllSubscribes(memberEntity.getMemberId(), PageRequest.of(0, 10));

    // Then
    assertThat(result).hasSize(1);
  }
}
