package com.onetuks.modulereader.subscribe.service;

import com.onetuks.modulecommon.error.ErrorCode;
import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.subscribe.entity.SubscribeEntity;
import com.onetuks.modulepersistence.subscribe.repository.SubscribeJpaRepository;
import com.onetuks.modulereader.author.service.AuthorService;
import com.onetuks.modulereader.member.service.MemberService;
import com.onetuks.modulereader.subscribe.service.dto.param.SubscribePostParam;
import com.onetuks.modulereader.subscribe.service.dto.result.SubscribeResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public SubscribeResult createSubscribe(long memberId, SubscribePostParam param) {
    AuthorEntity authorEntity = authorService.getAuthorById(param.authorId());
    authorEntity.getAuthorStaticsEntity().increaseSubscribeCount();

    return SubscribeResult.from(
        subscribeJpaRepository.save(
            SubscribeEntity.builder()
                .member(memberService.getMemberById(memberId))
                .author(authorEntity)
                .build()));
  }

  @Transactional
  public void deleteSubcribe(long memberId, long subscribeId) {
    SubscribeEntity subscribeEntity = getSubscribeById(subscribeId);

    if (subscribeEntity.getMemberEntity().getMemberId() != memberId) {
      throw new ApiAccessDeniedException(ErrorCode.UNAUTHORITY_ACCESS_DENIED);
    }

    subscribeEntity.getAuthorEntity().getAuthorStaticsEntity().decreaseSubscribeCount();
    subscribeJpaRepository.deleteById(subscribeId);
  }

  @Transactional(readOnly = true)
  public boolean readIsSubscribedAuthor(long memberId, long authorId) {
    return subscribeJpaRepository.existsByMemberEntityMemberIdAndAuthorEntityAuthorId(memberId, authorId);
  }

  @Transactional(readOnly = true)
  public Page<SubscribeResult> readAllSubscribes(long memberId, Pageable pageable) {
    return subscribeJpaRepository
        .findAllByMemberEntityMemberId(memberId, pageable)
        .map(SubscribeResult::from);
  }

  private SubscribeEntity getSubscribeById(long subscribeId) {
    return subscribeJpaRepository
        .findById(subscribeId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구독입니다."));
  }
}
