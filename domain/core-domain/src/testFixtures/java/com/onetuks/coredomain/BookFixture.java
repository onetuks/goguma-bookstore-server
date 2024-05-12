package com.onetuks.coredomain;

import com.onetuks.filestorage.vo.FileType;
import com.onetuks.filestorage.vo.FileWrapper;
import com.onetuks.filestorage.vo.FileWrapper.FileWrapperCollection;
import com.onetuks.filestorage.fixture.FileWrapperFixture;
import com.onetuks.filestorage.util.RandomValueProvider;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.entity.embedded.BookConceptualEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPhysicalInfoEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPriceInfoEmbedded;
import com.onetuks.dbstorage.book.vo.Category;
import java.util.List;
import java.util.Random;

public class BookFixture {

  public static BookEntity create(AuthorEntity authorEntity) {
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorEntity.getAuthorId(), FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorEntity.getAuthorId(), FileType.PREVIEWS);

    return BookEntity.builder()
        .author(authorEntity)
        .authorNickname(authorEntity.getNickname())
        .bookConceptualInfo(createBookConceptualInfo())
        .bookPhysicalInfo(createBookPhysicalInfo())
        .bookPriceInfo(createBookPriceInfo())
        .coverImgFilePath(coverImgFile.getUri())
        .detailImgFilePaths(detailImgFiles.getUris())
        .previewFilePaths(previewFiles.getUris())
        .build();
  }

  private static BookConceptualEmbedded createBookConceptualInfo() {
    return BookConceptualEmbedded.builder()
        .title(RandomValueProvider.createTitle())
        .oneLiner(RandomValueProvider.createOneLiner())
        .summary(RandomValueProvider.createSummary())
        .categories(createCategories())
        .publisher(RandomValueProvider.createPublisher())
        .isbn(RandomValueProvider.createIsbn())
        .build();
  }

  private static BookPhysicalInfoEmbedded createBookPhysicalInfo() {
    return BookPhysicalInfoEmbedded.builder()
        .height(RandomValueProvider.createHeight())
        .width(RandomValueProvider.createWidth())
        .coverType(RandomValueProvider.createCoverType())
        .pageCount(RandomValueProvider.createPageCount())
        .build();
  }

  private static BookPriceInfoEmbedded createBookPriceInfo() {
    return BookPriceInfoEmbedded.builder()
        .regularPrice(RandomValueProvider.createRegularPrice())
        .purchasePrice(RandomValueProvider.createPurchasePrice())
        .promotion(RandomValueProvider.createPromotion())
        .stockCount(RandomValueProvider.createStockCount())
        .build();
  }

  private static List<Category> createCategories() {
    Random random = new Random();
    int count = random.nextInt(1, 4);
    return random
        .ints(0, Category.values().length)
        .distinct()
        .limit(count)
        .boxed()
        .map(index -> Category.values()[index])
        .toList();
  }
}
