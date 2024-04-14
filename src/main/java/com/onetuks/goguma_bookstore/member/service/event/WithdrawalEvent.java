package com.onetuks.goguma_bookstore.member.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WithdrawalEvent extends ApplicationEvent {

  private final String token;

  public WithdrawalEvent(Object source, String token) {
    super(source);
    this.token = token;
  }
}
