package com.onetuks.scmapi.controller;

import com.onetuks.coreauth.util.author.AuthorLoginId;
import com.onetuks.filestorage.vo.FileType;
import com.onetuks.coreobj.vo.FileWrapper;
import com.onetuks.coreobj.vo.FileWrapper.FileWrapperCollection;
import com.onetuks.scmapi.controller.dto.request.BookEditRequest;
import com.onetuks.scmapi.controller.dto.response.BookEditResponse;
import com.onetuks.scmapi.controller.dto.response.BookResponse.BookResponses;
import com.onetuks.modulescm.book.service.BookScmService;
import com.onetuks.modulescm.book.service.dto.result.BookEditResult;
import com.onetuks.modulescm.book.service.dto.result.BookResult;
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
   * 도서 정보 수정 - 재고량 0으로 변경 (품절 -> 주문/장바구니 불가) - 해당 신간등록 검수 대기 변경 - 해당 신간등록 정보 수정
   *
   * @param authorLoginId : 로그인한 작가 아이디
   * @param bookId : 도서 아이디
   * @param request : 도서 수정 요청
   * @param coverImgFile : 커버 이미지
   * @param detailImgFiles : 상세 이미지
   * @param previewFiles : 미리보기 파일
   * @return BookEditResponse
   */
  @PatchMapping(
      path = "/{bookId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookEditResponse> editBook(
      @AuthorLoginId Long authorLoginId,
      @PathVariable(name = "bookId") Long bookId,
      @RequestBody BookEditRequest request,
      @RequestPart(name = "coverImgFile", required = false) MultipartFile coverImgFile,
      @RequestPart(name = "detailImgFiles", required = false) MultipartFile[] detailImgFiles,
      @RequestPart(name = "previewFiles", required = false) MultipartFile[] previewFiles) {
    BookEditResult result =
        bookScmService.updateBook(
            authorLoginId,
            bookId,
            request.to(),
            FileWrapper.of(authorLoginId, FileType.COVERS, coverImgFile),
            FileWrapperCollection.of(authorLoginId, FileType.DETAILS, detailImgFiles),
            FileWrapperCollection.of(authorLoginId, FileType.PREVIEWS, previewFiles));
    BookEditResponse response = BookEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 별 등록 도서 조회
   *
   * @param authorLoginId : 로그인한 작가 아이디
   * @param authorId : 작가 아이디
   * @param pageable : 페이지 정보
   * @return BookResponses
   */
  @GetMapping(path = "/scm/books?authorId=xx")
  public ResponseEntity<BookResponses> getAllBooksByAuthor(
      @AuthorLoginId Long authorLoginId,
      @RequestParam(name = "authorId") Long authorId,
      @PageableDefault(sort = "bookId", direction = Direction.DESC) Pageable pageable) {
    Page<BookResult> result = bookScmService.getAllBooksByAuthor(authorLoginId, authorId, pageable);
    BookResponses responses = BookResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
