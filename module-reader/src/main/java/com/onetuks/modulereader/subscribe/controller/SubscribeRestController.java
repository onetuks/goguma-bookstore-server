package com.onetuks.modulereader.subscribe.controller;

import com.onetuks.moduleauth.util.login.LoginId;
import com.onetuks.modulereader.subscribe.controller.dto.request.SubscribePostRequest;
import com.onetuks.modulereader.subscribe.controller.dto.response.SubscribeResponse;
import com.onetuks.modulereader.subscribe.service.SubscribeService;
import com.onetuks.modulereader.subscribe.service.dto.result.SubscribePostResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/subscribes")
public class SubscribeRestController {

  private final SubscribeService subscribeService;

  public SubscribeRestController(SubscribeService subscribeService) {
    this.subscribeService = subscribeService;
  }

  /**
   * 작가 구독 신청
   *
   * @param loginId : 로그인 아이디
   * @param request : 구독 신청 요청
   * @return SubscribeResponse
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscribeResponse> postSubscribe(
      @LoginId Long loginId, @RequestBody @Valid SubscribePostRequest request) {
    SubscribePostResult result = subscribeService.createSubscribe(loginId, request.to());
    SubscribeResponse response = SubscribeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping(path = "/{subscribeId}")
  public ResponseEntity<Void> cancelSubscribe(
      @LoginId Long loginId, @PathVariable(name = "subscribeId") Long subscribeId) {
    subscribeService.deleteSubcribe(loginId, subscribeId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
