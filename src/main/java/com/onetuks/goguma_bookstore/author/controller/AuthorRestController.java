package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.admin.AdminId;
import com.onetuks.goguma_bookstore.auth.util.login.LoginId;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorCreateEnrollmentRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorCreateEnrollmentResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentJudgeResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEscrowServiceHandOverResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorMailOrderSalesSubmitResponse;
import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/authors")
public class AuthorRestController {

  private final AuthorService authorService;

  public AuthorRestController(AuthorService authorService) {
    this.authorService = authorService;
  }

  /**
   * 작가 등록 신청
   *
   * @param loginId
   * @param request
   * @return
   */
  @PostMapping(
      path = "/enrollment",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorCreateEnrollmentResponse> requestEnrollment(
      @LoginId Long loginId, @Valid @RequestBody AuthorCreateEnrollmentRequest request) {
    AuthorCreateEnrollmentResult result =
        authorService.createAuthorEnrollment(loginId, request.to());
    AuthorCreateEnrollmentResponse response = AuthorCreateEnrollmentResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 구매안전서비스확인증 발급 - only for admin
   *
   * @param adminId
   * @param authorId
   * @param escrowServiceFile
   * @return
   */
  @PatchMapping(
      path = "/enrollment/{authorId}/escrow-service",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEscrowServiceHandOverResponse> handOverEscrowService(
      @AdminId Long adminId,
      @PathVariable("authorId") Long authorId,
      @RequestPart(name = "escrow-service-file") MultipartFile escrowServiceFile) {
    AuthorEscrowServiceHandOverResult result =
        authorService.editAuthorEscrowService(authorId, escrowServiceFile);
    AuthorEscrowServiceHandOverResponse response = AuthorEscrowServiceHandOverResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 통신판매신고증 전송
   *
   * @param loginId
   * @param authorId
   * @param mailOrderSalesFile
   * @return
   */
  @PatchMapping(
      path = "/enrollment/{authorId}/main-order-sales",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorMailOrderSalesSubmitResponse> submitMailOrderSales(
      @LoginId Long loginId,
      @PathVariable(name = "authorId") Long authorId,
      @RequestPart(name = "mail-order-sales") MultipartFile mailOrderSalesFile) {
    AuthorMailOrderSalesSubmitResult result =
        authorService.editAuthorMailOrderSales(loginId, authorId, mailOrderSalesFile);
    AuthorMailOrderSalesSubmitResponse response = AuthorMailOrderSalesSubmitResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 입접 심사 - only for admin todo 해당 멤버에게 알람 보내기
   *
   * @param adminId
   * @param authorId
   * @return
   */
  @PatchMapping(
      path = "/enrollment/{authorId}/result",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEnrollmentJudgeResponse> judgeEnrollment(
      @AdminId Long adminId, @PathVariable(name = "authorId") Long authorId) {
    AuthorEnrollmentJudgeResult result = authorService.editAuthorEnrollmentJudge(authorId);
    AuthorEnrollmentJudgeResponse response = AuthorEnrollmentJudgeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
