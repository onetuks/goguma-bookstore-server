package com.onetuks.readerdomain.restock.service;

import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coredomain.restock.repository.RestockRepository;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestockService {

  private final RestockRepository restockRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;

  public RestockService(
      RestockRepository restockRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository) {
    this.restockRepository = restockRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public Restock createRestock(long memberId, long bookId) {
    Member member = memberRepository.read(memberId);

    return restockRepository.create(
        new Restock(null, member, bookRepository.read(bookId), false, member.isAlarmPermitted()));
  }

  @Transactional(readOnly = true)
  public Page<Restock> readAllRestocks(long memberId, Pageable pageable) {
    return restockRepository.readAll(memberId, pageable);
  }

  @Transactional
  public Restock updateRestockAlarm(long memberId, long restockId, boolean isAlarmPermitted) {
    Restock restock = restockRepository.read(restockId);

    if (restock.member().memberId() != memberId) {
      throw new ApiAccessDeniedException("해당 재입고에 대한 권한이 없는 멤버입니다.");
    }

    return restockRepository.update(restock.changeAlarmPermitted(isAlarmPermitted));
  }

  @Transactional
  public void deleteRestock(long memberId, long restockId) {
    long targetMemberId = restockRepository.read(restockId).member().memberId();

    if (targetMemberId != memberId) {
      throw new ApiAccessDeniedException("해당 재입고에 대한 권한이 없는 멤버입니다.");
    }

    restockRepository.delete(restockId);
  }
}
