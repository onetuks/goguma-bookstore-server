package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.admin.AdminId;
import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.auth.util.login.LoginId;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorCreateEnrollmentRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorCreateEnrollmentResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentDetailsResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentDetailsResponse.AuthorEnrollmentDetailsResponses;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentJudgeResponse;
import com.onetuks.goguma_bookstore.author.service.AuthorEnrollmentService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/authors/enrollment")
public class AuthorEnrollmentRestController {

  private final AuthorEnrollmentService authorEnrollmentService;

  public AuthorEnrollmentRestController(AuthorEnrollmentService authorEnrollmentService) {
    this.authorEnrollmentService = authorEnrollmentService;
  }

  /**
   * 작가 등록 신청
   *
   * @param loginId : 토큰 인증 정보
   * @param request : 작가 등록 신청 요청
   * @return authorId
   */
  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorCreateEnrollmentResponse> requestEnrollment(
      @LoginId Long loginId, @RequestBody @Valid AuthorCreateEnrollmentRequest request) {
    AuthorCreateEnrollmentResult result =
        authorEnrollmentService.createAuthorEnrollment(loginId, request.to());
    AuthorCreateEnrollmentResponse response = AuthorCreateEnrollmentResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 입점 심사 - only for admin todo 해당 멤버에게 알람 보내기
   *
   * @param adminId : 관리자 확인용
   * @param authorId : 작가 등록 식별자
   * @return AuthorEnrollmentJudgeResponse
   */
  @PatchMapping(
      path = "/{authorId}/result",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentJudgeResponse> judgeEnrollment(
      @AdminId Long adminId, @PathVariable(name = "authorId") Long authorId) {
    AuthorEnrollmentJudgeResult result =
        authorEnrollmentService.updateAuthorEnrollmentJudge(authorId);
    AuthorEnrollmentJudgeResponse response = AuthorEnrollmentJudgeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 입점 신청 취소
   *
   * @param loginAuthorId : 본인 확인용
   * @param authorId : 작가 등록 식별자
   * @return void
   */
  @DeleteMapping(path = "/{authorId}")
  public ResponseEntity<Void> cancelAuthorEnrollment(
      @AuthorId Long loginAuthorId, @PathVariable(name = "authorId") Long authorId) {
    authorEnrollmentService.deleteAuthorEnrollment(loginAuthorId, authorId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 작가 등록 상세 조회
   *
   * @param loginAuthorId : 본인 확인용
   * @param authorId : 작가 등록 식별자
   * @return authorId, memberId, roleType, profileImgUrl, nickname, introduction, escrowServiceUrl,
   *     mailOrderSalesUrl, enrollmentPassed, enrollmentAt
   */
  @GetMapping(path = "/{authorId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentDetailsResponse> getAuthorEnrollmentDetails(
      @AuthorId Long loginAuthorId, @PathVariable(name = "authorId") Long authorId) {
    AuthorEnrollmentDetailsResult result =
        authorEnrollmentService.findAuthorEnrollmentDetails(loginAuthorId, authorId);
    AuthorEnrollmentDetailsResponse response = AuthorEnrollmentDetailsResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 모든 작가 등록 상세 조회 - only for admin
   *
   * @param adminId : 관리자 확인용
   * @return authorId, memberId, roleType, profileImgUrl, nickname, introduction, *
   *     escrowServiceUrl, mailOrderSalesUrl, enrollmentPassed, enrollmentAt
   */
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentDetailsResponses> getAllAuthorEnrollmentDetails(
      @AdminId Long adminId,
      @PageableDefault(sort = "enrollmentAt", direction = Direction.DESC) Pageable pageable) {
    Page<AuthorEnrollmentDetailsResult> results =
        authorEnrollmentService.findAllAuthorEnrollmentDetails(pageable);
    AuthorEnrollmentDetailsResponses responses = AuthorEnrollmentDetailsResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
