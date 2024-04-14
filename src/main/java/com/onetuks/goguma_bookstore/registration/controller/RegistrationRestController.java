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
