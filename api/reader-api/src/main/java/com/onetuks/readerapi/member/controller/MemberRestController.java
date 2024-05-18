package com.onetuks.readerapi.member.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.readerapi.member.dto.request.MemberPatchRequest;
import com.onetuks.readerapi.member.dto.response.MemberPatchResponse;
import com.onetuks.readerapi.member.dto.response.MemberGetResponse;
import com.onetuks.readerdomain.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
   * 멤버 프로필 정보 수정
   * @param memberId : 멤버 ID
   * @param request : 멤버 수정 요청 내용
   * @param profileImgFile : 프로필 이미지 파일
   * @return 200 OK
   */
  @PatchMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberPatchResponse> editMemberProfile(
      @MemberId Long memberId,
      @RequestBody @Valid MemberPatchRequest request,
      @RequestPart(value = "profile-img-file", required = false) MultipartFile profileImgFile
  ) {
    Member result =
        memberService.updateMemberProfile(
            memberId,
            request.to(),
            FileWrapper.of(FileType.PROFILES, UUIDProvider.provideUUID(), profileImgFile));
    MemberPatchResponse response = MemberPatchResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 멤버 프로필 정보 단건 조회
   * @param memberId : 멤버 ID
   * @return 200 OK
   */
  @GetMapping(path = "/{memberId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberGetResponse> getMemberInfo(
      @PathVariable(name = "memberId") Long memberId
  ) {
    Member result = memberService.readMemberDetails(memberId);
    MemberGetResponse response = MemberGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
