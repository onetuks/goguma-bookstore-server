package com.onetuks.readerdomain.member.param;

public record MemberEditParam(
    String nickname,
    Boolean isAlarmPermitted,
    String defaultAddress,
    String defaultAddressDetail) {}
