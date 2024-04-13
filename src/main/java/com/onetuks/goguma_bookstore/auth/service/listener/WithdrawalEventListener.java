package com.onetuks.goguma_bookstore.auth.service.listener;

import com.onetuks.goguma_bookstore.auth.jwt.AuthToken;
import com.onetuks.goguma_bookstore.auth.jwt.AuthTokenProvider;
import com.onetuks.goguma_bookstore.auth.service.AuthService;
import com.onetuks.goguma_bookstore.member.service.event.WithdrawalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class WithdrawalEventListener {

  private final AuthService authService;
  private final AuthTokenProvider authTokenProvider;

  public WithdrawalEventListener(AuthService authService, AuthTokenProvider authTokenProvider) {
    this.authService = authService;
    this.authTokenProvider = authTokenProvider;
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleWithdrawalEvent(WithdrawalEvent event) {
    log.info("회원탈퇴 이벤트 수신");

    AuthToken authToken = authTokenProvider.convertToAuthToken(event.getToken());
    authService.logout(authToken);
  }
}
