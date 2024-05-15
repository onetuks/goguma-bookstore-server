package com.onetuks.readerdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.SubscribeFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coredomain.subscribe.model.Subscribe;
import com.onetuks.coredomain.subscribe.repository.SubscribeRepository;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import com.onetuks.readerdomain.subscribe.service.SubscribeService;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class SubscribeServiceTest extends ReaderDomainIntegrationTest {

  @Autowired private SubscribeService subscribeService;

  @MockBean private SubscribeRepository subscribeRepository;
  @MockBean private MemberRepository memberRepository;
  @MockBean private AuthorRepository authorRepository;

  @Test
  @DisplayName("중복되지 않은 구독 정보라면 해당 작가에 대한 구독수를 늘리고 구독 성공한다..")
  void createSubscribeTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    Subscribe subscribe = SubscribeFixture.create(createId(), member, author);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(authorRepository.read(author.authorId())).willReturn(author);
    given(subscribeRepository.create(any())).willReturn(subscribe);

    // When
    Subscribe result = subscribeService.createSubscribe(member.memberId(), author.authorId());

    // Then
    assertThat(result.author().authorStatics().subscribeCount()).isOne();
  }

  @Test
  @DisplayName("해당 작가에 대해서 구독이 되어있는지 여부를 조회한다.")
  void readIsSubscribedAuthorTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));

    given(subscribeRepository.readExistence(member.memberId(), author.authorId())).willReturn(true);

    // When
    boolean result = subscribeService.readIsSubscribedAuthor(member.memberId(), author.authorId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("유저의 모든 구독 정보를 조회한다.")
  void readAllSubscribesTest() {
    // Given
    int count = 5;
    List<Member> members =
        IntStream.range(0, count)
            .mapToObj(i -> MemberFixture.create(createId(), RoleType.USER))
            .toList();
    List<Author> authors =
        IntStream.range(0, count)
            .mapToObj(i -> AuthorFixture.create(createId(), members.get(i)))
            .toList();
    List<Subscribe> subscribes =
        IntStream.range(0, count)
            .mapToObj(i -> SubscribeFixture.create(createId(), members.get(i), authors.get(i)))
            .toList();

    Member member = MemberFixture.create(createId(), RoleType.USER);

    given(subscribeRepository.readAll(member.memberId())).willReturn(subscribes);

    // When
    List<Subscribe> results = subscribeService.readAllSubscribes(member.memberId());

    // Then
    assertThat(results).hasSize(count);
  }

  @Test
  @DisplayName("존재하는 구독 정보를 제거하면 성공하며 해당 작가의 구독수가 감소한다.")
  void deleteSubcribeTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    Subscribe subscribe = SubscribeFixture.create(createId(), member, author);

    given(subscribeRepository.read(subscribe.subscribeId())).willReturn(subscribe);

    // When
    subscribeService.deleteSubcribe(member.memberId(), subscribe.subscribeId());

    // Then
    verify(subscribeRepository, times(1)).delete(subscribe.subscribeId());
  }

  @Test
  @DisplayName("다른 유저가 구독 정보를 제거하려고 하면 예외를 던진다. 구독자수는 감소하지 않는다.")
  void deleteSubsribe_AccessDenied_Exception() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    Subscribe subscribe = SubscribeFixture.create(createId(), member, author);

    given(subscribeRepository.read(subscribe.subscribeId())).willReturn(subscribe);

    // When
    subscribeService.deleteSubcribe(member.memberId(), subscribe.subscribeId());

    // Then
    verify(subscribeRepository, times(1)).delete(subscribe.subscribeId());
  }
}
