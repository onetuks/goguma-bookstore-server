package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createCategories;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createCoverType;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createHeight;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createIsbn;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createOneLiner;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createPageCount;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createPromotion;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createPublisher;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createPurchasePrice;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createRegularPrice;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createStockCount;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createSummary;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createTitle;
import static com.onetuks.goguma_bookstore.util.RandomValueProvider.createWidth;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.vo.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPriceInfo;
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
        .publisher(createPublisher())
        .stockCount(createStockCount())
        .coverImgFile(coverImgFile.toCoverImgFile())
        .detailImgFiles(detailImgFiles.stream().map(CustomFile::toDetailImgFile).toList())
        .previewFiles(previewFiles.stream().map(CustomFile::toPreviewFile).toList())
        .build();
  }

  private static BookConceptualInfo createBookConceptualInfo() {
    return BookConceptualInfo.builder()
        .title(createTitle())
        .oneLiner(createOneLiner())
        .summary(createSummary())
        .categories(createCategories())
        .isbn(createIsbn())
        .build();
  }

  private static BookPhysicalInfo createBookPhysicalInfo() {
    return BookPhysicalInfo.builder()
        .height(createHeight())
        .width(createWidth())
        .coverType(createCoverType())
        .pageCount(createPageCount())
        .build();
  }

  private static BookPriceInfo createBookPriceInfo() {
    return BookPriceInfo.builder()
        .regularPrice(createRegularPrice())
        .purchasePrice(createPurchasePrice())
        .promotion(createPromotion())
        .build();
  }
}
