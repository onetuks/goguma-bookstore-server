package com.onetuks.readerapi.controller;

import com.onetuks.coreauth.util.login.LoginId;
import com.onetuks.readerapi.controller.dto.response.FavoriteGetResponse.FavoriteGetResponses;
import com.onetuks.readerapi.controller.dto.response.FavoritePostResponse;
import com.onetuks.readerapi.controller.dto.response.FavoriteWhetherGetResponse;
import com.onetuks.modulereader.favorite.service.FavoriteService;
import com.onetuks.modulereader.favorite.service.dto.result.FavoriteGetResult;
import com.onetuks.modulereader.favorite.service.dto.result.FavoritePostResult;
import com.onetuks.modulereader.favorite.service.dto.result.FavoriteWhetherGetResult;
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
   * @param memberId : 로그인한 회원의 ID
   * @param bookId : 즐겨찾기에 추가할 도서의 ID
   * @return FavoritePostResponse
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FavoritePostResponse> addFavorite(
      @LoginId Long memberId, @RequestParam(name = "bookId") Long bookId) {
    FavoritePostResult result = favoriteService.createFavorite(memberId, bookId);
    FavoritePostResponse response = FavoritePostResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 즐겨찾기 삭제
   *
   * @param memberId : 로그인 아이디
   * @param favoriteId : 즐겨찾기 아이디
   * @return Void
   */
  @DeleteMapping(path = "/{favoriteId}")
  public ResponseEntity<Void> removeFavorite(
      @LoginId Long memberId, @PathVariable(name = "favoriteId") Long favoriteId) {
    favoriteService.deleteFavorite(memberId, favoriteId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 즐겨찾기 여부 조회
   *
   * @param memberId : 로그인한 회원의 ID
   * @param bookId : 즐겨찾기 여부를 조회할 도서의 ID
   * @return FavoriteWhetherGetResponse
   */
  @GetMapping(path = "/whether", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FavoriteWhetherGetResponse> getIsFavorited(
      @LoginId Long memberId, @RequestParam(name = "bookId") Long bookId) {
    FavoriteWhetherGetResult result = favoriteService.readFavoriteExistence(memberId, bookId);
    FavoriteWhetherGetResponse response = FavoriteWhetherGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 즐겨찾기 목록 조회
   *
   * @param memberId : 로그인한 회원의 ID
   * @param pageable : 페이지 정보
   * @return FavoriteGetResponses
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FavoriteGetResponses> getResponsesOfMember(
      @LoginId Long memberId,
      @PageableDefault(sort = "favoriteId", direction = Direction.DESC) Pageable pageable) {
    Page<FavoriteGetResult> results = favoriteService.readFavoritesOfMember(memberId, pageable);
    FavoriteGetResponses responses = FavoriteGetResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
