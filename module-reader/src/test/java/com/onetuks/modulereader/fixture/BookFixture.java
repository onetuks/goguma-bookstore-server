package com.onetuks.modulereader.fixture;

import static com.onetuks.modulereader.util.RandomValueProvider.*;

import com.onetuks.modulereader.global.vo.file.FileType;
import com.onetuks.modulereader.global.vo.file.FileWrapper;
import com.onetuks.modulereader.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.model.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPriceInfo;
import com.onetuks.modulereader.util.RandomValueProvider;

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
        .title(RandomValueProvider.createTitle())
        .oneLiner(RandomValueProvider.createOneLiner())
        .summary(RandomValueProvider.createSummary())
        .categories(RandomValueProvider.createCategories())
        .publisher(RandomValueProvider.createPublisher())
        .isbn(RandomValueProvider.createIsbn())
        .build();
  }

  private static BookPhysicalInfo createBookPhysicalInfo() {
    return BookPhysicalInfo.builder()
        .height(RandomValueProvider.createHeight())
        .width(RandomValueProvider.createWidth())
        .coverType(RandomValueProvider.createCoverType())
        .pageCount(RandomValueProvider.createPageCount())
        .build();
  }

  private static BookPriceInfo createBookPriceInfo() {
    return BookPriceInfo.builder()
        .regularPrice(RandomValueProvider.createRegularPrice())
        .purchasePrice(RandomValueProvider.createPurchasePrice())
        .promotion(RandomValueProvider.createPromotion())
        .stockCount(RandomValueProvider.createStockCount())
        .build();
  }
}
