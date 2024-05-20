package com.onetuks.readerapi.subscribe.controller;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.subscribe.model.Subscribe;
import com.onetuks.readerapi.subscribe.dto.response.SubscribeResponse;
import com.onetuks.readerapi.subscribe.dto.response.SubscribeResponse.SubscribeResponses;
import com.onetuks.readerdomain.subscribe.service.SubscribeService;
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
   * 작가 구독
   *
   * @param memberId : 로그인한 멤버 ID
   * @param authorId : 작가 ID
   * @return 200 OK
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscribeResponse> postNewSubscribe(
      @MemberId Long memberId, @RequestParam(name = "authorId") Long authorId) {
    Subscribe result = subscribeService.createSubscribe(memberId, authorId);
    SubscribeResponse response = SubscribeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 구독 취소
   *
   * @param memberId : 로그인한 멤버 ID
   * @param subscribeId : 구독 ID
   * @return 204 NO_CONTENT
   */
  @DeleteMapping(path = "/{subscribeId}")
  public ResponseEntity<Void> cancelSubscribe(
      @MemberId Long memberId, @PathVariable(name = "subscribeId") Long subscribeId) {
    subscribeService.deleteSubcribe(memberId, subscribeId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 구독 여부 조회
   *
   * @param memberId : 로그인한 멤버 ID
   * @param authorId : 작가 ID
   * @return 200 OK
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> getIsSubscribedAuthor(
      @MemberId Long memberId, @RequestParam(name = "authorId") Long authorId) {
    boolean response = subscribeService.readIsSubscribedAuthor(memberId, authorId);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 내 구독 목록 조회
   *
   * @param memberId : 로그인한 멤버 ID
   * @param pageable : 페이징 정보
   * @return 200 OK
   */
  @GetMapping(path = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscribeResponses> getAllMySubscribes(
      @MemberId Long memberId,
      @PageableDefault(sort = "subscribeId", direction = Direction.DESC) Pageable pageable) {
    Page<Subscribe> result = subscribeService.readAllSubscribes(memberId, pageable);
    SubscribeResponses responses = SubscribeResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
