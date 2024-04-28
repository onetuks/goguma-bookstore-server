package com.onetuks.goguma_bookstore.book.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.BookFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.ReviewFixture;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.model.Review;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

  @Test
  void createTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Book book =
        bookJpaRepository.save(
            BookFixture.create(authorJpaRepository.save(AuthorFixture.create(member))));

    // When
    Review review = ReviewFixture.create(book, member);

    // Then
    List<String> result = review.getReviewImgUrls();

    assertThat(result).isNotEmpty();
  }
}
