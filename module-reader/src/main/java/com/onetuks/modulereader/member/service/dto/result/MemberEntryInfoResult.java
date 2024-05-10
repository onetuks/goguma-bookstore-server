package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.entity.MemberEntity;

public record MemberEntryInfoResult(long memberId, String nickname, boolean alarmPermission) {

  public static MemberEntryInfoResult from(MemberEntity memberEntity) {
    return new MemberEntryInfoResult(
        memberEntity.getMemberId(), memberEntity.getNickname(), memberEntity.getAlarmPermission());
  }
}
