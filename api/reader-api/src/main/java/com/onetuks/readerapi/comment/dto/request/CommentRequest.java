package com.onetuks.readerapi.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CommentRequest(
    @NotBlank @Length(min = 1, max = 50) String title,
    @NotBlank @Length(min = 10, max = 1000) String content) {}
