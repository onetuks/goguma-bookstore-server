package com.onetuks.goguma_bookstore.book.controller;

import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.book.controller.dto.request.BookEditRequest;
import com.onetuks.goguma_bookstore.book.controller.dto.response.BookEditResponse;
import com.onetuks.goguma_bookstore.book.service.BookScmService;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookEditResult;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/scm/books")
public class BookScmRestController {

  private final BookScmService bookScmService;

  public BookScmRestController(BookScmService bookScmService) {
    this.bookScmService = bookScmService;
  }

  @PatchMapping(
      path = "/{bookId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookEditResponse> editBook(
      @AuthorId Long authorId,
      @PathVariable(name = "bookId") Long bookId,
      @RequestBody BookEditRequest request,
      @RequestPart(name = "coverImgFile", required = false) MultipartFile coverImgFile,
      @RequestPart(name = "detailImgFiles", required = false) MultipartFile[] detailImgFiles,
      @RequestPart(name = "previewFiles", required = false) MultipartFile[] previewFiles) {
    BookEditResult result =
        bookScmService.updateBook(
            authorId,
            bookId,
            request.to(),
            CustomFile.of(authorId, FileType.COVERS, coverImgFile),
            CustomFile.of(authorId, FileType.DETAILS, detailImgFiles),
            CustomFile.of(authorId, FileType.PREVIEWS, previewFiles));
    BookEditResponse response = BookEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
