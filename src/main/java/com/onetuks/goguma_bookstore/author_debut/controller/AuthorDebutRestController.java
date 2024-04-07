package com.onetuks.goguma_bookstore.author_debut.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.LoginId;
import com.onetuks.goguma_bookstore.author_debut.controller.dto.request.AuthorDebutCreateRequest;
import com.onetuks.goguma_bookstore.author_debut.controller.dto.response.AuthorDebutCreateResponse;
import com.onetuks.goguma_bookstore.author_debut.service.AuthorDebutService;
import com.onetuks.goguma_bookstore.author_debut.service.dto.result.AuthorDebutCreateResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/enrolls")
public class AuthorDebutRestController {

  private final AuthorDebutService authorDebutService;

  public AuthorDebutRestController(AuthorDebutService authorDebutService) {
    this.authorDebutService = authorDebutService;
  }

  /**
   * 작가 등록 신청
   *
   * @param loginId
   * @param request
   * @return
   */
  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDebutCreateResponse> createEnrollRequest(
      @LoginId Long loginId, @Valid @RequestBody AuthorDebutCreateRequest request) {
    AuthorDebutCreateResult result = authorDebutService.createAuthorDebut(loginId, request.to());
    AuthorDebutCreateResponse response = AuthorDebutCreateResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
