package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.auth.vo.RoleType;

public record AuthorEnrollmentJudgeResult(
    boolean enrollmentPassed, long memberId, RoleType roleType) {}
