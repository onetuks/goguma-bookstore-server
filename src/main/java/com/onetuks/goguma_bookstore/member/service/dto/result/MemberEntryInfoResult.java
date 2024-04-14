package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.goguma_bookstore.member.model.Member;

public record MemberEntryInfoResult(long memberId, String nickname, boolean alarmPermission) {

  public static MemberEntryInfoResult from(Member member) {
    return new MemberEntryInfoResult(
        member.getMemberId(), member.getNickname(), member.getAlarmPermission());
  }
}
