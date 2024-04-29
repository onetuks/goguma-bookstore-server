package com.onetuks.modulereader.member.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WithdrawalEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public WithdrawalEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void publishWithdrawalEvent(String token) {
    log.info("회원탈퇴 이벤트 발행");
    WithdrawalEvent withdrawalEvent = new WithdrawalEvent(this, token);
    eventPublisher.publishEvent(withdrawalEvent);
  }
}
