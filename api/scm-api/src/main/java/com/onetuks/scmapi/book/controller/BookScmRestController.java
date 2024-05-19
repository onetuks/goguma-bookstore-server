package com.onetuks.scmapi.book.controller;

import com.onetuks.coreauth.util.author.AuthorId;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.scmapi.book.dto.request.BookPatchRequest;
import com.onetuks.scmapi.book.dto.response.BookPatchResponse;
import com.onetuks.scmapi.book.dto.response.BookResponse.BookResponses;
import com.onetuks.scmdomain.book.service.BookScmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  /**
   * 도서 정보 수정
   *
   * @param authorLoginId : 로그인한 작가 ID
   * @param bookId : 책 ID
   * @param request : 책 수정 요청 내용
   * @param coverImgFile : 표지 이미지 파일
   * @param detailImgFiles : 상세 이미지 파일
   * @param previewFiles : 미리보기 파일
   * @return 200 OK
   */
  @PatchMapping(
      path = "/{bookId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookPatchResponse> editBook(
      @AuthorId Long authorLoginId,
      @PathVariable(name = "bookId") Long bookId,
      @RequestBody BookPatchRequest request,
      @RequestPart(name = "coverImgFile", required = false) MultipartFile coverImgFile,
      @RequestPart(name = "detailImgFiles", required = false) MultipartFile[] detailImgFiles,
      @RequestPart(name = "previewFiles", required = false) MultipartFile[] previewFiles) {
    String uuid = UUIDProvider.provideUUID();
    Book result =
        bookScmService.updateBook(
            authorLoginId,
            bookId,
            request.to(),
            FileWrapper.of(FileType.COVERS, uuid, coverImgFile),
            FileWrapperCollection.of(FileType.DETAILS, uuid, detailImgFiles),
            FileWrapperCollection.of(FileType.PREVIEWS, uuid, previewFiles));
    BookPatchResponse response = BookPatchResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가별 도서 목록 조회
   *
   * @param authorLoginId : 로그인한 작가 ID
   * @param authorId : 작가 ID
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(path = "/scm/books?authorId=xx")
  public ResponseEntity<BookResponses> getAllBooksByAuthor(
      @AuthorId Long authorLoginId,
      @RequestParam(name = "authorId") Long authorId,
      @PageableDefault(sort = "bookId", direction = Direction.DESC) Pageable pageable) {
    Page<Book> result = bookScmService.readAllBooksByAuthor(authorLoginId, authorId, pageable);
    BookResponses responses = BookResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
