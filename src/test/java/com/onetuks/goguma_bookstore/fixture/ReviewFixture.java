package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.model.Review;
import com.onetuks.modulepersistence.member.model.Member;

public class ReviewFixture {

  public static Review create(Book book, Member member) {
    FileWrapperCollection reviewImgFiles =
        FileWrapperFixture.createFiles(member.getMemberId(), FileType.REVIEWS);

    return Review.builder()
        .book(book)
        .member(member)
        .score(4.5F)
        .content("암튼 저녁먹을때 보면 밥도둑임")
        .reviewImgFilePaths(reviewImgFiles.getUris())
        .build();
  }
}
