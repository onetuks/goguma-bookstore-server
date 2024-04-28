package com.onetuks.goguma_bookstore.registration.controller;

import com.onetuks.goguma_bookstore.auth.util.admin.AdminId;
import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.FileWrapper;
import com.onetuks.goguma_bookstore.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationCreateRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationEditRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationInspectionRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationInspectionResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationIsbnResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationResponse;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationResponse.RegistrationResponses;
import com.onetuks.goguma_bookstore.registration.service.RegistrationScmService;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationResult;
import com.onetuks.goguma_bookstore.registration.service.verification.IsbnWebClientService;
import com.onetuks.goguma_bookstore.registration.service.verification.dto.result.RegistrationIsbnResult;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/scm/registrations")
public class RegistrationScmRestController {

  private final RegistrationScmService registrationScmService;
  private final IsbnWebClientService isbnWebClientService;

  public RegistrationScmRestController(
      RegistrationScmService registrationScmService, IsbnWebClientService isbnWebClientService) {
    this.registrationScmService = registrationScmService;
    this.isbnWebClientService = isbnWebClientService;
  }

  /**
   * 신간 등록 신청 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param request : 신간 등록 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param detailImgFiles : 신간 모의 이미지 파일
   * @param previewFiles : 신간 미리보기 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return RegistrationResponse
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponse> addRegistration(
      @AuthorId Long authorId,
      @RequestBody @Valid RegistrationCreateRequest request,
      @RequestPart(name = "cover-img-file") MultipartFile coverImgFile,
      @RequestPart(name = "detail-img-files") MultipartFile[] detailImgFiles,
      @RequestPart(name = "preview-files") MultipartFile[] previewFiles,
      @RequestPart(name = "sample-file") MultipartFile sampleFile) {
    RegistrationResult result =
        registrationScmService.createRegistration(
            authorId,
            request.to(),
            FileWrapper.of(authorId, FileType.COVERS, coverImgFile),
            FileWrapperCollection.of(authorId, FileType.DETAILS, detailImgFiles),
            FileWrapperCollection.of(authorId, FileType.PREVIEWS, previewFiles),
            FileWrapper.of(authorId, FileType.SAMPLES, sampleFile));
    RegistrationResponse response = RegistrationResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 검수 - 관리자용
   *
   * @param adminId : 로그인한 관리자 아이디
   * @param registrationId : 신간 등록 아이디
   * @param request : 신간 등록 검수 요청 정보
   * @return RegistrationInspectionResponse
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
        registrationScmService.updateRegistrationApprovalInfo(
            registrationId, request.approvalResult(), request.approvalMemo());
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
   * @return RegistrationResponse
   */
  @PatchMapping(
      path = "/{registrationId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponse> editRegistration(
      @AuthorId Long authorId,
      @PathVariable(name = "registrationId") Long registrationId,
      @RequestBody @Valid RegistrationEditRequest request,
      @RequestPart(name = "cover-img-file", required = false) MultipartFile coverImgFile,
      @RequestPart(name = "detail-img-files", required = false) MultipartFile[] detailImgFiles,
      @RequestPart(name = "preview-files", required = false) MultipartFile[] previewFiles,
      @RequestPart(name = "sample-file", required = false) MultipartFile sampleFile) {
    RegistrationResult result =
        registrationScmService.updateRegistration(
            registrationId,
            request.to(),
            FileWrapper.of(authorId, FileType.COVERS, coverImgFile),
            FileWrapperCollection.of(authorId, FileType.DETAILS, detailImgFiles),
            FileWrapperCollection.of(authorId, FileType.PREVIEWS, previewFiles),
            FileWrapper.of(authorId, FileType.SAMPLES, sampleFile));
    RegistrationResponse response = RegistrationResponse.from(result);

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
    registrationScmService.deleteRegistration(authorId, registrationId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 신간 등록 조회 - 작가용
   *
   * @param authorId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @return RegistrationResponse
   */
  @GetMapping(path = "/{registrationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponse> getRegistration(
      @AuthorId Long authorId, @PathVariable(name = "registrationId") Long registrationId) {
    RegistrationResult result = registrationScmService.readRegistration(authorId, registrationId);
    RegistrationResponse response = RegistrationResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 전체 조회 - 관리자용
   *
   * @param adminId : 로그인한 관리자 아이디
   * @return RegistrationResponses
   */
  @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponses> getAllRegistrations(
      @AdminId Long adminId,
      @PageableDefault(sort = "registrationId", direction = Direction.DESC) Pageable pageable) {
    Page<RegistrationResult> result = registrationScmService.readAllRegistrations(pageable);
    RegistrationResponses response = RegistrationResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가별 신간 등록 조회 - 관리자용
   *
   * @param loginAuthorId : 로그인한 작가 아이디
   * @param authorId : 작가 아이디
   * @return RegistrationResponses
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponses> getAllRegistrationsByAuthor(
      @AuthorId Long loginAuthorId,
      @RequestParam(name = "authorId") Long authorId,
      @PageableDefault(sort = "registrationId", direction = Direction.DESC) Pageable pageable) {
    Page<RegistrationResult> result =
        registrationScmService.readAllRegistrationsByAuthor(loginAuthorId, authorId, pageable);
    RegistrationResponses response = RegistrationResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * ISBN으로 책 정보 조회
   *
   * @param isbn : ISBN
   * @return RegistrationIsbnResponse
   */
  @GetMapping(path = "/isbn/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationIsbnResponse> getBookInfoByIsbn(
      @PathVariable(name = "isbn") String isbn) {
    RegistrationIsbnResult result = isbnWebClientService.requestData(isbn);
    RegistrationIsbnResponse response = RegistrationIsbnResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
