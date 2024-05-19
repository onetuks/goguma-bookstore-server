package com.onetuks.readerdomain.member.param;

public record MemberPatchParam(
    String nickname,
    Boolean isAlarmPermitted,
    String defaultAddress,
    String defaultAddressDetail) {}
