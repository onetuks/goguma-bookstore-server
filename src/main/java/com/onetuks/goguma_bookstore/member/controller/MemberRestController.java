package com.onetuks.goguma_bookstore.member.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.login.LoginId;
import com.onetuks.goguma_bookstore.member.controller.dto.request.MemberEntryInfoRequest;
import com.onetuks.goguma_bookstore.member.controller.dto.response.MemberEntryInfoResponse;
import com.onetuks.goguma_bookstore.member.service.MemberService;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/members")
public class MemberRestController {

  private final MemberService memberService;

  public MemberRestController(MemberService memberService) {
    this.memberService = memberService;
  }

  /**
   * 회원가입 정보 입력
   *
   * @param memberId : 로그인한 회원 ID
   * @param request : 회원가입 정보
   * @return memberId, nickname, alarmPermission
   */
  @PatchMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberEntryInfoResponse> entryMemberInfo(
      @LoginId Long memberId, @RequestBody @Valid MemberEntryInfoRequest request) {
    MemberEntryInfoResult result = memberService.entryMemberInfo(memberId, request.to());
    MemberEntryInfoResponse response = MemberEntryInfoResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
