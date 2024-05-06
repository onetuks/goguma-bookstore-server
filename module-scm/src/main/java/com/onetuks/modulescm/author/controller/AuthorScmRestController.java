package com.onetuks.modulescm.author.controller;

import com.onetuks.moduleauth.util.admin.AdminLoginId;
import com.onetuks.moduleauth.util.author.AuthorLoginId;
import com.onetuks.moduleauth.util.login.LoginId;
import com.onetuks.modulescm.author.controller.dto.request.AuthorCreateEnrollmentRequest;
import com.onetuks.modulescm.author.controller.dto.response.AuthorCreateEnrollmentResponse;
import com.onetuks.modulescm.author.controller.dto.response.AuthorEnrollmentDetailsResponse;
import com.onetuks.modulescm.author.controller.dto.response.AuthorEnrollmentDetailsResponse.AuthorEnrollmentDetailsResponses;
import com.onetuks.modulescm.author.controller.dto.response.AuthorEnrollmentJudgeResponse;
import com.onetuks.modulescm.author.service.AuthorScmService;
import com.onetuks.modulescm.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentJudgeResult;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/scm/authors/enrollment")
public class AuthorScmRestController {

  private final AuthorScmService authorScmService;

  public AuthorScmRestController(AuthorScmService authorScmService) {
    this.authorScmService = authorScmService;
  }

  /**
   * 작가 등록 신청
   *
   * @param loginId : 토큰 인증 정보
   * @param request : 작가 등록 신청 요청
   * @return AuthorCreateEnrollmentResponse
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorCreateEnrollmentResponse> requestEnrollment(
      @LoginId Long loginId, @RequestBody @Valid AuthorCreateEnrollmentRequest request) {
    AuthorCreateEnrollmentResult result =
        authorScmService.createAuthorEnrollment(loginId, request.to());
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
      path = "/{authorId}/judge",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentJudgeResponse> judgeEnrollment(
      @AdminLoginId Long adminId, @PathVariable(name = "authorId") Long authorId) {
    AuthorEnrollmentJudgeResult result = authorScmService.updateAuthorEnrollmentJudge(authorId);
    AuthorEnrollmentJudgeResponse response = AuthorEnrollmentJudgeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 등록 취소
   *
   * @param authorLoginId : 본인 확인용
   * @return 204 No Content
   */
  @DeleteMapping
  public ResponseEntity<Void> cancelAuthorEnrollment(@AuthorLoginId Long authorLoginId) {
    authorScmService.deleteAuthorEnrollment(authorLoginId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 작가 등록 상세 조회
   *
   * @param loginAuthorId : 본인 확인용
   * @param authorId : 작가 등록 식별자
   * @return authorId, memberId, roleType, profileImgUrl, nickname, introduction, businessNumber,
   *     mailOrderSalesUrl, enrollmentPassed, enrollmentAt
   */
  @GetMapping(path = "/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentDetailsResponse> getAuthorEnrollmentDetails(
      @AuthorLoginId Long loginAuthorId, @PathVariable(name = "authorId") Long authorId) {
    AuthorEnrollmentDetailsResult result =
        authorScmService.readAuthorEnrollmentDetails(loginAuthorId, authorId);
    AuthorEnrollmentDetailsResponse response = AuthorEnrollmentDetailsResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 모든 작가 등록 상세 조회 - only for admin
   *
   * @param adminId : 관리자 확인용
   * @return authorId, memberId, roleType, profileImgUrl, nickname, introduction, * businessNumber,
   *     mailOrderSalesUrl, enrollmentPassed, enrollmentAt
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentDetailsResponses> getAllAuthorEnrollmentDetails(
      @AdminLoginId Long adminId,
      @PageableDefault(sort = "enrollmentAt", direction = Direction.DESC) Pageable pageable) {
    Page<AuthorEnrollmentDetailsResult> results =
        authorScmService.readAllAuthorEnrollmentDetails(pageable);
    AuthorEnrollmentDetailsResponses responses = AuthorEnrollmentDetailsResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
