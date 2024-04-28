package com.onetuks.goguma_bookstore.fixture;

import static com.onetuks.goguma_bookstore.util.RandomValueProvider.*;

import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.FileWrapper;
import com.onetuks.goguma_bookstore.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.model.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPriceInfo;

public class BookFixture {

  public static Book create(Author author) {
    FileWrapper coverImgFile = FileWrapperFixture.createFile(author.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);

    return Book.builder()
        .author(author)
        .authorNickname(author.getNickname())
        .bookConceptualInfo(createBookConceptualInfo())
        .bookPhysicalInfo(createBookPhysicalInfo())
        .bookPriceInfo(createBookPriceInfo())
        .coverImgFilePath(coverImgFile.getUri())
        .detailImgFilePaths(detailImgFiles.getUris())
        .previewFilePaths(previewFiles.getUris())
        .build();
  }

  private static BookConceptualInfo createBookConceptualInfo() {
    return BookConceptualInfo.builder()
        .title(createTitle())
        .oneLiner(createOneLiner())
        .summary(createSummary())
        .categories(createCategories())
        .publisher(createPublisher())
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
        .stockCount(createStockCount())
        .build();
  }
}
