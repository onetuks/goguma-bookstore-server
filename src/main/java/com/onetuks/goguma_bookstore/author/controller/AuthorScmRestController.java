package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.admin.AdminId;
import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.auth.util.login.LoginId;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorCreateEnrollmentRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorEditRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorCreateEnrollmentResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEditResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentDetailsResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentDetailsResponse.AuthorEnrollmentDetailsResponses;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentJudgeResponse;
import com.onetuks.goguma_bookstore.author.service.AuthorScmService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.FileWrapper;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
   * @return authorId
   */
  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
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
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentJudgeResponse> judgeEnrollment(
      @AdminId Long adminId, @PathVariable(name = "authorId") Long authorId) {
    AuthorEnrollmentJudgeResult result = authorScmService.updateAuthorEnrollmentJudge(authorId);
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
    authorScmService.deleteAuthorEnrollment(loginAuthorId, authorId);

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
        authorScmService.readAuthorEnrollmentDetails(loginAuthorId, authorId);
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
        authorScmService.readAllAuthorEnrollmentDetails(pageable);
    AuthorEnrollmentDetailsResponses responses = AuthorEnrollmentDetailsResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 작가 프로필 수정
   *
   * @param loginAuthorId : 로그인한 작가 ID
   * @param authorId : 수정할 작가 ID
   * @param request : 작가 프로필 수정 내용
   * @param profileImgFile : 작가 프로필 수정 이미지 (같은 이미지여도 덮어쓰기)
   * @return authorId, profileImgUrl, nickname, introduction
   */
  @PatchMapping(
      path = "/{authorId}",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEditResponse> editAuthorProfile(
      @AuthorId Long loginAuthorId,
      @PathVariable(name = "authorId") Long authorId,
      @Valid @RequestBody AuthorEditRequest request,
      @RequestPart(name = "profile-img-file", required = false) MultipartFile profileImgFile) {
    AuthorEditResult result =
        authorScmService.updateAuthorProfile(
            loginAuthorId,
            authorId,
            request.to(),
            FileWrapper.of(loginAuthorId, FileType.PROFILES, profileImgFile));
    AuthorEditResponse response = AuthorEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
