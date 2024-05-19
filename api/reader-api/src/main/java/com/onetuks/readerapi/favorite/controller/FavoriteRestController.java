package com.onetuks.readerapi.favorite.controller;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.readerapi.favorite.dto.response.FavoriteGetResponse.FavoriteGetResponses;
import com.onetuks.readerapi.favorite.dto.response.FavoritePostResponse;
import com.onetuks.readerdomain.favorite.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/favorites")
public class FavoriteRestController {

  private final FavoriteService favoriteService;

  public FavoriteRestController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  /**
   * 즐겨찾기 추가
   *
   * @param memberId : 로그인한 멤버 ID
   * @param bookId : 도서 ID
   * @return 200 OK
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FavoritePostResponse> postNewFavorite(
      @MemberId Long memberId, @RequestParam(name = "bookId") Long bookId) {
    Favorite result = favoriteService.createFavorite(memberId, bookId);
    FavoritePostResponse response = FavoritePostResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 즐겨찾기 취소
   *
   * @param memberId : 로그인한 멤버 ID
   * @param favoriteId : 즐겨찾기 ID
   * @return 204 NO_CONTENT
   */
  @DeleteMapping(path = "/{favoriteId}")
  public ResponseEntity<Void> cancelFavorite(
      @MemberId Long memberId, @PathVariable(name = "favoriteId") Long favoriteId) {
    favoriteService.deleteFavorite(memberId, favoriteId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 즐겨찾기 여부 조회
   *
   * @param memberId : 로그인한 멤버 ID
   * @param bookId : 도서 ID
   * @return 200 OK
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> getIsFavoritedBook(
      @MemberId Long memberId, @RequestParam(name = "bookId") Long bookId) {
    boolean result = favoriteService.readFavoriteExistence(memberId, bookId);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 내 즐겨찾기 목록 조회
   *
   * @param memberId : 로그인한 멤버 ID
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(path = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FavoriteGetResponses> getAllMyFavorites(
      @MemberId Long memberId,
      @PageableDefault(sort = "favoriteId", direction = Direction.DESC) Pageable pageable) {
    Page<Favorite> results = favoriteService.readAllFavorites(memberId, pageable);
    FavoriteGetResponses responses = FavoriteGetResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
