package com.onetuks.goguma_bookstore.registration.controller;

import com.onetuks.goguma_bookstore.auth.util.admin.AdminId;
import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationCreateRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationEditRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationInspectionRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationCreateResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationEditResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationGetResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationGetResponse.RegistrationGetResponses;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationInspectionResponse;
import com.onetuks.goguma_bookstore.registration.service.RegistrationService;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
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
   * 신간 등록 신청 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param request : 신간 등록 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param mockUpFiles : 신간 모의 이미지 파일
   * @param previewFiles : 신간 미리보기 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return registrationId, approvalResult, approvalMemo, title, oneLiner, summary, categories,
   *     publisher, isbn, pageSizeInfo, coverType, pageCount, price, stockCount, promotion,
   *     coverImgUrl, detailImgUrls, previewUrls, sampleUrl
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationCreateResponse> requestRegistration(
      @AuthorId Long authorId,
      @RequestBody @Valid RegistrationCreateRequest request,
      @RequestPart(name = "cover-img-file") MultipartFile coverImgFile,
      @RequestPart(name = "mock-up-files") MultipartFile[] mockUpFiles,
      @RequestPart(name = "preview-files") MultipartFile[] previewFiles,
      @RequestPart(name = "sample-file") MultipartFile sampleFile) {
    RegistrationCreateResult result =
        registrationService.createRegistration(
            authorId,
            request.to(),
            CustomFile.of(authorId, FileType.COVERS, coverImgFile),
            CustomFile.of(authorId, FileType.DETAILS, mockUpFiles),
            CustomFile.of(authorId, FileType.PREVIEWS, previewFiles),
            CustomFile.of(authorId, FileType.SAMPLES, sampleFile));
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

  /**
   * 신간 등록 수정 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @param request : 신간 등록 수정 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return registrationId, title, summary, price, stockCount, isbn, publisher, coverImgUrl,
   *     sampleUrl
   */
  @PatchMapping(
      path = "/{registrationId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationEditResponse> editRegistration(
      @AuthorId Long authorId,
      @PathVariable(name = "registrationId") Long registrationId,
      @RequestBody @Valid RegistrationEditRequest request,
      @RequestPart(name = "cover-img-file") MultipartFile coverImgFile,
      @RequestPart(name = "sample-file") MultipartFile sampleFile) {
    RegistrationEditResult result =
        registrationService.updateRegistration(
            registrationId,
            request.to(),
            CustomFile.of(authorId, FileType.COVERS, coverImgFile),
            CustomFile.of(authorId, FileType.SAMPLES, sampleFile));
    RegistrationEditResponse response = RegistrationEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 삭제 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @return Void
   */
  @DeleteMapping(path = "/{registrationId}")
  public ResponseEntity<Void> removeRegistration(
      @AuthorId Long authorId, @PathVariable(name = "registrationId") Long registrationId) {
    registrationService.deleteRegistration(authorId, registrationId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 신간 등록 조회 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @return registrationId, approvalResult, approvalMemo, coverImgUrl, title, summary, price,
   *     stockCount, isbn, publisher, sampleUrl
   */
  @GetMapping(path = "/{registrationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationGetResponse> getRegistration(
      @AuthorId Long authorId, @PathVariable(name = "registrationId") Long registrationId) {
    RegistrationGetResult result = registrationService.getRegistration(authorId, registrationId);
    RegistrationGetResponse response = RegistrationGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 전체 조회 - 관리자용
   *
   * @param adminId : 로그인한 관리자 아이디
   * @return registrationId, approvalResult, approvalMemo, coverImgUrl, title, summary, price,
   *     stockCount, isbn, publisher, promotion, sampleUrl
   */
  @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationGetResponses> getAllRegistrations(@AdminId Long adminId) {
    List<RegistrationGetResult> result = registrationService.getAllRegistrations();
    RegistrationGetResponses response = RegistrationGetResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가별 신간 등록 조회 - 관리자용
   *
   * @param loginAuthorId : 로그인한 작가 아이디
   * @param authorId : 작가 아이디
   * @return registrationId, approvalResult, approvalMemo, coverImgUrl, title, summary, price,
   *     stockCount, isbn, publisher, promotion, sampleUrl
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationGetResponses> getAllRegistrationByAuthor(
      @AuthorId Long loginAuthorId, @RequestParam(name = "authorId") Long authorId) {
    List<RegistrationGetResult> result =
        registrationService.getAllRegistrationsByAuthor(loginAuthorId, authorId);
    RegistrationGetResponses response = RegistrationGetResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
