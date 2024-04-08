package com.onetuks.goguma_bookstore.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorServiceTest extends IntegrationTest {

  @Autowired private AuthorService authorService;
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
    AuthorCreateParam param = AuthorFixture.createCreationParam();

    // When
    AuthorCreateResult result = authorService.createAuthorDebut(member.getMemberId(), param);

    // Then
    assertAll(
        "createAuthorDebutTest",
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }
}
