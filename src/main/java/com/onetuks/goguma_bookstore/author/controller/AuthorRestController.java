package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorEditRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEditResponse;
import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PatchMapping(
      path = "/{authorId}",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEditResponse> editAuthorProfile(
      @AuthorId Long loginAuthorId,
      @PathVariable(name = "authorId") Long authorId,
      @Valid @RequestBody AuthorEditRequest request,
      @RequestPart(name = "profileImg", required = false) MultipartFile profileImg) {
    AuthorEditResult result =
        authorService.updateAuthorProfile(loginAuthorId, authorId, request.to(), profileImg);
    AuthorEditResponse response = AuthorEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
