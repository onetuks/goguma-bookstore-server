package com.onetuks.readerapi.controller;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.modulereader.subscribe.service.SubscribeService;
import com.onetuks.modulereader.subscribe.service.dto.result.SubscribeResult;
import com.onetuks.readerapi.controller.dto.request.SubscribePostRequest;
import com.onetuks.readerapi.controller.dto.response.SubscribeResponse;
import com.onetuks.readerapi.controller.dto.response.SubscribeResponse.SubscribeResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      @MemberId Long loginId, @RequestBody @Valid SubscribePostRequest request) {
    SubscribeResult result = subscribeService.createSubscribe(loginId, request.to());
    SubscribeResponse response = SubscribeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 구독 취소
   *
   * @param loginId : 로그인 아이디
   * @param subscribeId : 구독 아이디
   * @return 204 No Content
   */
  @DeleteMapping(path = "/{subscribeId}")
  public ResponseEntity<Void> cancelSubscribe(
      @MemberId Long loginId, @PathVariable(name = "subscribeId") Long subscribeId) {
    subscribeService.deleteSubcribe(loginId, subscribeId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 작가 구독 여부 조회
   *
   * @param loginId : 로그인 아이디
   * @param authorId : 작가 아이디
   * @return SubscribeResponse
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> getIsSubscribedAuthor(
      @MemberId Long loginId, @RequestParam(name = "authorId") Long authorId) {
    boolean response = subscribeService.readIsSubscribedAuthor(loginId, authorId);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping(path = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscribeResponses> getAllMySubscribes(
      @MemberId Long loginId,
      @PageableDefault(sort = "subscribeId", direction = Direction.DESC) Pageable pageable) {
    Page<SubscribeResult> result = subscribeService.readAllSubscribes(loginId, pageable);
    SubscribeResponses responses = SubscribeResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
