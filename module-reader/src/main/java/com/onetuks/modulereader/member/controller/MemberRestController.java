package com.onetuks.modulereader.member.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulereader.auth.jwt.AuthHeaderUtil;
import com.onetuks.modulereader.auth.util.login.LoginId;
import com.onetuks.modulereader.member.controller.dto.request.MemberDefaultAddressEditRequest;
import com.onetuks.modulereader.member.controller.dto.request.MemberDefaultCashReceiptEditRequest;
import com.onetuks.modulereader.member.controller.dto.request.MemberEntryInfoRequest;
import com.onetuks.modulereader.member.controller.dto.request.MemberProfileEditRequest;
import com.onetuks.modulereader.member.controller.dto.response.MemberDefaultAddressEditResponse;
import com.onetuks.modulereader.member.controller.dto.response.MemberDefaultCashReceiptEditResponse;
import com.onetuks.modulereader.member.controller.dto.response.MemberEntryInfoResponse;
import com.onetuks.modulereader.member.controller.dto.response.MemberInfoResponse;
import com.onetuks.modulereader.member.controller.dto.response.MemberProfileEditResponse;
import com.onetuks.modulereader.member.service.MemberService;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultAddressEditResult;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import com.onetuks.modulereader.member.service.dto.result.MemberEntryInfoResult;
import com.onetuks.modulereader.member.service.dto.result.MemberInfoResult;
import com.onetuks.modulereader.member.service.dto.result.MemberProfileEditResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  @PatchMapping(
      path = "/entry",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberEntryInfoResponse> entryMemberInfo(
      @LoginId Long memberId, @RequestBody @Valid MemberEntryInfoRequest request) {
    MemberEntryInfoResult result = memberService.updateMemberInfo(memberId, request.to());
    MemberEntryInfoResponse response = MemberEntryInfoResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 회원 프로필 수정
   *
   * @param memberId : 로그인한 회원 ID
   * @param request : 수정할 회원 프로필 정보
   * @param profileImgFile : 프로필 이미지
   * @return memberId, nickname, alarmPermission, profileImgUri, defaultAddress,
   *     defaultAddressDetail, defaultCashReceiptType, defaultCashReceiptNumber
   */
  @PatchMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberProfileEditResponse> editMemberProfile(
      @LoginId Long memberId,
      @RequestBody @Valid MemberProfileEditRequest request,
      @RequestPart(value = "profile-img-file", required = false) MultipartFile profileImgFile) {
    MemberProfileEditResult result =
        memberService.updateMemberProfile(
            memberId, request.to(), FileWrapper.of(memberId, FileType.PROFILES, profileImgFile));
    MemberProfileEditResponse response = MemberProfileEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 회원 탈퇴
   *
   * @param memberId : 로그인한 회원 ID
   * @param request : HttpServletRequest
   * @return void
   */
  @DeleteMapping
  public ResponseEntity<Void> withdrawMember(@LoginId Long memberId, HttpServletRequest request) {
    memberService.deleteMember(memberId, AuthHeaderUtil.extractAuthToken(request));

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 기본 배송지 설정
   *
   * @param memberId : 로그인한 멤버 아이디
   * @param request : 기본 배송지 수정 요청
   * @return defaultAddress, defaultAddressDetail
   */
  @PatchMapping(
      path = "/default-address",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberDefaultAddressEditResponse> editDefaultAddress(
      @LoginId Long memberId, @RequestBody @Valid MemberDefaultAddressEditRequest request) {
    MemberDefaultAddressEditResult result =
        memberService.updateDetaultAddress(memberId, request.to());
    MemberDefaultAddressEditResponse response = MemberDefaultAddressEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 기본 현금영수증 설정
   *
   * @param memberId : 로그인한 멤버 아이디
   * @param request : 기본 현금영수증 수정 요청
   * @return defaultCashReceiptType, defaultCashReceiptNumber
   */
  @PatchMapping(
      path = "/default-cash-receipt",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberDefaultCashReceiptEditResponse> editDefaultCashReceipt(
      @LoginId Long memberId, @RequestBody @Valid MemberDefaultCashReceiptEditRequest request) {
    MemberDefaultCashReceiptEditResult result =
        memberService.updateDefaultCashReceipt(memberId, request.to());
    MemberDefaultCashReceiptEditResponse response =
        MemberDefaultCashReceiptEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 멤버 정보 조회
   *
   * @param memberId : 조회할 멤버 아이디
   * @return memberId, nickname, alarmPermission, profileImgUri, defaultAddress,
   *     defaultAddressDetail, defaultCashReceiptType, defaultCashReceiptNumber
   */
  @GetMapping(path = "/{memberId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberInfoResponse> getMemberInfo(
      @PathVariable(name = "memberId") Long memberId) {
    MemberInfoResult result = memberService.readMemberInfo(memberId);
    MemberInfoResponse response = MemberInfoResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
