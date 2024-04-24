package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.Review;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.member.model.Member;
import java.util.List;

public class ReviewFixture {

  public static Review create(Book book, Member member) {
    List<CustomFile> reviewImgFiles =
        CustomFileFixture.createFiles(member.getMemberId(), FileType.REVIEWS);

    return Review.builder()
        .book(book)
        .member(member)
        .score(4.5F)
        .content("암튼 저녁먹을때 보면 밥도둑임")
        .reviewImgFiles(reviewImgFiles.stream().map(CustomFile::toReivewFile).toList())
        .build();
  }
}
