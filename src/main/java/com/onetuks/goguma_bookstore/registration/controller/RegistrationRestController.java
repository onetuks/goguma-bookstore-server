package com.onetuks.goguma_bookstore.registration.controller;

import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.registration.controller.dto.request.RegistrationPostRequest;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationPostResponse;
import com.onetuks.goguma_bookstore.registration.service.RegistrationService;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationPostResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
   * 신간 등록을 요청
   *
   * @param authorId : 로그인한 작가 아이디
   * @param request : 신간 등록 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return ResponseEntity<RegistrationPostResponse>
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationPostResponse> requestRegistration(
      @AuthorId Long authorId,
      @RequestBody @Valid RegistrationPostRequest request,
      @RequestPart(name = "cover-img-file") MultipartFile coverImgFile,
      @RequestPart(name = "sample-file") MultipartFile sampleFile) {
    RegistrationPostResult result =
        registrationService.createRegistration(
            authorId,
            request.to(),
            CustomFile.of(authorId, FileType.BOOK_COVERS, coverImgFile),
            CustomFile.of(authorId, FileType.BOOK_SAMPLES, sampleFile));
    RegistrationPostResponse response = RegistrationPostResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
