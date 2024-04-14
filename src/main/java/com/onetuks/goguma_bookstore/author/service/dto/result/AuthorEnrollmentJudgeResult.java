package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;

public record AuthorEnrollmentJudgeResult(
    boolean enrollmentPassed, long memberId, RoleType roleType) {}
