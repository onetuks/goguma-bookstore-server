package com.onetuks.goguma_bookstore.author_debut.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.author_debut.service.dto.param.AuthorDebutCreateParam;
import com.onetuks.goguma_bookstore.author_debut.service.dto.result.AuthorDebutCreateResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorDebutServiceTest extends IntegrationTest {

  @Autowired private AuthorDebutService authorDebutService;
  @Autowired private MemberRepository memberRepository;

  private Member member;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(MemberFixture.create());
  }

  @Test
  @DisplayName("작가 등록 신청을 수행한다.")
  void createAuthorDebutTest() throws IOException {
    // Given
    AuthorDebutCreateParam param = AuthorFixture.createCreationParam();

    // When
    AuthorDebutCreateResult result =
        authorDebutService.createAuthorDebut(member.getMemberId(), param);

    // Then
    assertAll(
        "createAuthorDebutTest",
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }
}
