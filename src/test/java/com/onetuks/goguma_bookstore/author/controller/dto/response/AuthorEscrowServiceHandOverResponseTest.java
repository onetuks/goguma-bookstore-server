package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import org.junit.jupiter.api.Test;

class AuthorEscrowServiceHandOverResponseTest {

  @Test
  void fromTest() {
    // Given
    AuthorEscrowServiceHandOverResult authorEscrowServiceHandOverResult =
        new AuthorEscrowServiceHandOverResult("구매안전증.pdf");

    // When
    AuthorEscrowServiceHandOverResponse result =
        AuthorEscrowServiceHandOverResponse.from(authorEscrowServiceHandOverResult);

    // Then
    assertThat(result.escrowServiceUrl())
        .isEqualTo(authorEscrowServiceHandOverResult.escrowServiceUrl());
  }
}
