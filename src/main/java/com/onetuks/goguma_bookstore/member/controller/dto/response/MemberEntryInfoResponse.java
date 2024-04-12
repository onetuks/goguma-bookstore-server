package com.onetuks.goguma_bookstore.member.controller.dto.response;

import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;

public record MemberEntryInfoResponse(long memberId, String nickname, boolean alarmPermission) {

  public static MemberEntryInfoResponse from(MemberEntryInfoResult result) {
    return new MemberEntryInfoResponse(
        result.memberId(), result.nickname(), result.alarmPermission());
  }
}
