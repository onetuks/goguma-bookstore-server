package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.vo.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPriceInfo;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import java.util.List;

public class BookFixture {

  public static Book create(Author author) {
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> detailImgFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    List<CustomFile> previewFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);

    return Book.builder()
        .author(author)
        .authorNickname(author.getNickname())
        .bookConceptualInfo(createBookConceptualInfo())
        .bookPhysicalInfo(createBookPhysicalInfo())
        .bookPriceInfo(createBookPriceInfo())
        .publisher("아무거나보틀")
        .stockCount(100L)
        .coverImgFile(coverImgFile.toCoverImgFile())
        .detailImgFiles(detailImgFiles.stream().map(CustomFile::toDetailImgFile).toList())
        .previewFiles(previewFiles.stream().map(CustomFile::toPreviewFile).toList())
        .build();
  }

  private static BookConceptualInfo createBookConceptualInfo() {
    return BookConceptualInfo.builder()
        .title("빠니보틀의 유라시아 여행기")
        .oneLiner("유튜브 대통령의 유라시아 행차 일기")
        .summary("대충 태국에서 시작해서 유럽에서 끝났나? 기억 안 남")
        .categories(List.of(Category.NOVEL, Category.ESSEY))
        .isbn("9788912345678")
        .build();
  }

  private static BookPhysicalInfo createBookPhysicalInfo() {
    return BookPhysicalInfo.builder()
        .height(200)
        .width(100)
        .coverType("양장본")
        .pageCount(300L)
        .build();
  }

  private static BookPriceInfo createBookPriceInfo() {
    return BookPriceInfo.builder()
        .regularPrice(20_000L)
        .purchasePrice(10_000L)
        .promotion(true)
        .build();
  }
}
