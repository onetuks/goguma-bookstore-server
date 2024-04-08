package com.onetuks.goguma_bookstore.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

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
  void createAuthorEnrollmentTest() {
    // Given
    AuthorCreateParam param = AuthorFixture.createCreationParam();

    // When
    AuthorCreateResult result = authorService.createAuthorEnrollment(member.getMemberId(), param);

    // Then
    assertAll(
        "createAuthorDebutTest",
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("구매안전서비스확인증을 부여한다.")
  void editAuthorEscrowService() throws IOException {
    // Given
    AuthorCreateParam createParam = AuthorFixture.createCreationParam();
    AuthorCreateResult createResult =
        authorService.createAuthorEnrollment(member.getMemberId(), createParam);
    MultipartFile escrowServiceFile =
        MultipartFileFixture.createFile(FileType.ESCROWS, createResult.authorId());

    // When
    AuthorEscrowServiceHandOverResult result =
        authorService.editAuthorEscrowService(createResult.authorId(), escrowServiceFile);

    // Then
    assertThat(result.escrowServiceUrl()).contains(escrowServiceFile.getName());

    MultipartFileFixture.deleteFile(escrowServiceFile.getName());
  }
}
