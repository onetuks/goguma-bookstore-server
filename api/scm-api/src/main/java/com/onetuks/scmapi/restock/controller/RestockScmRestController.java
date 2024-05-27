package com.onetuks.scmapi.restock.controller;

import com.onetuks.coreauth.util.author.AuthorId;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.scmapi.restock.controller.dto.response.RestockScmResponse.RestockScmResponses;
import com.onetuks.scmdomain.restock.service.RestockScmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/scm/restocks")
public class RestockScmRestController {

  private final RestockScmService restockScmService;

  public RestockScmRestController(RestockScmService restockScmService) {
    this.restockScmService = restockScmService;
  }

  /**
   * 작가 별 재입고 필요 도서 수 조회
   *
   * @param authorMemberId : 작가 회원 ID
   * @return 200 OK
   */
  @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> getRestockedBookCount(@AuthorId Long authorMemberId) {
    long result = restockScmService.readRestockBookCount(authorMemberId);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 재입고 도서 목록 조회
   *
   * @param authorMemberId : 작가 회원 ID
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RestockScmResponses> getAllRestocks(
      @AuthorId Long authorMemberId,
      @PageableDefault(sort = "restockId", direction = Direction.ASC) Pageable pageable) {
    Page<Restock> results = restockScmService.readAllRestockBooks(authorMemberId, pageable);
    RestockScmResponses responses = RestockScmResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
