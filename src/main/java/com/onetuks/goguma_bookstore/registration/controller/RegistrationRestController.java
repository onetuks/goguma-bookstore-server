package com.onetuks.goguma_bookstore.registration.controller;

import com.onetuks.goguma_bookstore.auth.util.admin.AdminId;
import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationCreateRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationInspectionRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationCreateResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationInspectionResponse;
import com.onetuks.goguma_bookstore.registration.service.RegistrationService;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(path = "/registrations")
public class RegistrationRestController {

  private final RegistrationService registrationService;

  public RegistrationRestController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  /**
   * 신간 등록을 요청 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param request : 신간 등록 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return registrationId, approvalResult, approvalMemo, coverImgUrl, title, summary, price,
   *     stockCount, isbn, publisher, promotion, sampleUrl
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationCreateResponse> requestRegistration(
      @AuthorId Long authorId,
      @RequestBody @Valid RegistrationCreateRequest request,
      @RequestPart(name = "cover-img-file") MultipartFile coverImgFile,
      @RequestPart(name = "sample-file") MultipartFile sampleFile) {
    RegistrationCreateResult result =
        registrationService.createRegistration(
            authorId,
            request.to(),
            CustomFile.of(authorId, FileType.BOOK_COVERS, coverImgFile),
            CustomFile.of(authorId, FileType.BOOK_SAMPLES, sampleFile));
    RegistrationCreateResponse response = RegistrationCreateResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 검수 - 관리자용
   *
   * @param adminId : 로그인한 관리자 아이디
   * @param registrationId : 신간 등록 아이디
   * @param request : 신간 등록 검수 요청 정보
   * @return registrationId, approvalResult, approvalMemo
   */
  @PatchMapping(
      path = "/{registrationId}/ispection",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationInspectionResponse> inspectRegistration(
      @AdminId Long adminId,
      @PathVariable(name = "registrationId") Long registrationId,
      @RequestBody @Valid RegistrationInspectionRequest request) {
    RegistrationInspectionResult result =
        registrationService.updateRegistrationApproval(registrationId, request.to());
    RegistrationInspectionResponse response = RegistrationInspectionResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
