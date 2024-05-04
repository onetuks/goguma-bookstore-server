package com.onetuks.modulereader.author.service.dto.result;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import java.util.List;

public record AuthorEnrollmentJudgeResult(
    boolean enrollmentPassed,
    long memberId,
    List<RoleType> roleTypes
) {}
