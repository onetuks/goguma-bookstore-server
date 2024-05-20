package com.onetuks.readerapi.restock;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.readerapi.restock.dto.response.RestockResponse;
import com.onetuks.readerapi.restock.dto.response.RestockResponse.RestockResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/restocks")
public class RestockRestController {

  private final RestockService restockService;

  public RestockRestController(RestockService restockService) {
    this.restockService = restockService;
  }

  /**
   * 도서 재입고 신청
   * @param memberId : 로그인한 멤버 ID
   * @param bookId : 도서 ID
   * @return 200 OK
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RestockResponse> postNewRestock(
      @MemberId Long memberId,
      @RequestParam(name = "bookId") Long bookId) {
    Restock result = restockService.createRestock(memberId, bookId);
    RestockResponse response = RestockResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 재입고 취소
   * @param memberId : 로그인한 멤버 ID
   * @param restockId : 재입고 ID
   * @return 204 NO_CONTENT
   */
  @DeleteMapping(path = "/{restockId}")
  public ResponseEntity<Void> cancelRestock(
      @MemberId Long memberId, @PathVariable(name = "restockId") Long restockId) {
    restockService.cancelRestock(memberId, restockId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 멤버 모든 재입고 신청 조회
   * @param memberId : 로그인한 멤버 ID
   * @return 200 OK
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RestockResponses> getAllRestockOfMember(
      @MemberId Long memberId,
      @PageableDefault(sort = "restockId", direction = Direction.ASC) Pageable pageable
  ) {
    Page<Restock> results = restockService.readAllRestocks(memberId, pageable);
    RestockResponses responses = RestockResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 재입고 알림 설정
   * @param memberId : 로그인한 멤버 ID
   * @param restockId : 재입고 ID
   * @param isAlarmPermitted : 알림 허용 여부
   * @return 200 OK
   */
  @PatchMapping(path = "/{restockId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RestockResponse> patchRestockAlarm(
      @MemberId Long memberId,
      @PathVariable(name = "restockId") Long restockId,
      @RequestParam(name = "alarm") Boolean isAlarmPermitted
  ) {
    Restock result = restockService.updateRestockAlarm(memberId, restockId, isAlarmPermitted);
    RestockResponse response = RestockResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
