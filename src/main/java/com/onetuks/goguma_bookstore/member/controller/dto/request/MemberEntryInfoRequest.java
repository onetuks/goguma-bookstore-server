package com.onetuks.goguma_bookstore.member.controller.dto.request;

import com.onetuks.goguma_bookstore.member.service.dto.param.MemberEntryInfoParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MemberEntryInfoRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname, @NotNull Boolean alarmPermission) {

  public MemberEntryInfoParam to() {
    return new MemberEntryInfoParam(nickname(), alarmPermission());
  }
}
