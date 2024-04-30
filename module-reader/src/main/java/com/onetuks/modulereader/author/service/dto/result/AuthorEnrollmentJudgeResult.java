package com.onetuks.modulereader.author.service.dto.result;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;

public record AuthorEnrollmentJudgeResult(
    boolean enrollmentPassed, long memberId, RoleType roleType) {}