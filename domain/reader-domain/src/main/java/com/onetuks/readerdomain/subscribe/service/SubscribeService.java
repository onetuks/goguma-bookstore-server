package com.onetuks.readerdomain.subscribe.service;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coredomain.subscribe.model.Subscribe;
import com.onetuks.coredomain.subscribe.repository.SubscribeRepository;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscribeService {

  private final SubscribeRepository subscribeRepository;
  private final MemberRepository memberRepository;
  private final AuthorRepository authorRepository;

  public SubscribeService(
      SubscribeRepository subscribeRepository,
      MemberRepository memberRepository,
      AuthorRepository authorRepository) {
    this.subscribeRepository = subscribeRepository;
    this.memberRepository = memberRepository;
    this.authorRepository = authorRepository;
  }

  @Transactional
  public Subscribe createSubscribe(long memberId, long authorId) {
    Member member = memberRepository.read(memberId);
    Author author = authorRepository.read(authorId);

    return subscribeRepository.create(new Subscribe(null, member, author));
  }

  @Transactional(readOnly = true)
  public boolean readIsSubscribedAuthor(long memberId, long authorId) {
    return subscribeRepository.readExistence(memberId, authorId);
  }

  @Transactional(readOnly = true)
  public Page<Subscribe> readAllSubscribes(long memberId, Pageable pageable) {
    return subscribeRepository.readAll(memberId, pageable);
  }

  @Transactional
  public void deleteSubcribe(long memberId, long subscribeId) {
    long targetMemberId = subscribeRepository.read(subscribeId).member().memberId();

    if (targetMemberId != memberId) {
      throw new ApiAccessDeniedException("해당 구독을 취소할 권한이 없는 멤버입니다.");
    }

    subscribeRepository.delete(subscribeId);
  }
}
