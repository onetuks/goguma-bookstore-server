package com.onetuks.modulereader.subscribe.service;

import com.onetuks.modulecommon.error.ErrorCode;
import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.subscribe.model.Subscribe;
import com.onetuks.modulepersistence.subscribe.repository.SubscribeJpaRepository;
import com.onetuks.modulereader.author.service.AuthorService;
import com.onetuks.modulereader.member.service.MemberService;
import com.onetuks.modulereader.subscribe.service.dto.param.SubscribePostParam;
import com.onetuks.modulereader.subscribe.service.dto.result.SubscribePostResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscribeService {

  private final SubscribeJpaRepository subscribeJpaRepository;
  private final MemberService memberService;
  private final AuthorService authorService;

  public SubscribeService(
      SubscribeJpaRepository subscribeJpaRepository,
      MemberService memberService,
      AuthorService authorService) {
    this.subscribeJpaRepository = subscribeJpaRepository;
    this.memberService = memberService;
    this.authorService = authorService;
  }

  @Transactional
  public SubscribePostResult createSubscribe(long memberId, SubscribePostParam param) {
    Author author = authorService.getAuthorById(param.authorId());
    author.getAuthorStatics().increaseSubscribeCount();

    return SubscribePostResult.from(
        subscribeJpaRepository.save(
            Subscribe.builder()
                .member(memberService.getMemberById(memberId))
                .author(author)
                .build()));
  }

  public void deleteSubcribe(long memberId, long subscribeId) {
    Subscribe subscribe = getSubscribeById(subscribeId);

    if (subscribe.getMember().getMemberId() != memberId) {
      throw new ApiAccessDeniedException(ErrorCode.UNAUTHORITY_ACCESS_DENIED);
    }

    subscribe.getAuthor().getAuthorStatics().decreaseSubscribeCount();
    subscribeJpaRepository.deleteById(subscribeId);
  }

  private Subscribe getSubscribeById(long subscribeId) {
    return subscribeJpaRepository
        .findById(subscribeId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구독입니다."));
  }
}
