package com.onetuks.dbstorage.fixture;

import static com.onetuks.coredomain.CustomFilePathFixture.createCoverImgFilePath;
import static com.onetuks.coredomain.CustomFilePathFixture.createDetailImgFilePaths;
import static com.onetuks.coredomain.CustomFilePathFixture.createPreviewFilePaths;
import static com.onetuks.coredomain.CustomFilePathFixture.createSampleFilePath;
import static com.onetuks.coredomain.util.TestValueProvider.createApprovalMemo;
import static com.onetuks.coredomain.util.TestValueProvider.createCategories;
import static com.onetuks.coredomain.util.TestValueProvider.createCoverType;
import static com.onetuks.coredomain.util.TestValueProvider.createHeight;
import static com.onetuks.coredomain.util.TestValueProvider.createIsbn;
import static com.onetuks.coredomain.util.TestValueProvider.createOneLiner;
import static com.onetuks.coredomain.util.TestValueProvider.createPageCount;
import static com.onetuks.coredomain.util.TestValueProvider.createPrice;
import static com.onetuks.coredomain.util.TestValueProvider.createPromotion;
import static com.onetuks.coredomain.util.TestValueProvider.createPublisher;
import static com.onetuks.coredomain.util.TestValueProvider.createSalesRate;
import static com.onetuks.coredomain.util.TestValueProvider.createStockCount;
import static com.onetuks.coredomain.util.TestValueProvider.createSummary;
import static com.onetuks.coredomain.util.TestValueProvider.createTitle;
import static com.onetuks.coredomain.util.TestValueProvider.createWidth;

import com.onetuks.coreobj.vo.UUIDProvider;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.registration.entity.RegistrationEntity;

public class RegistrationEntityFixture {

  public static RegistrationEntity create(AuthorEntity authorEntity) {
    String uuid = UUIDProvider.provideUUID();
    return new RegistrationEntity(
        null,
        authorEntity,
        false,
        createApprovalMemo(false),
        createTitle(),
        createOneLiner(),
        createSummary(),
        createCategories(),
        createPublisher(),
        createIsbn(),
        createHeight(),
        createWidth(),
        createCoverType(),
        createPageCount(),
        createPrice(),
        createSalesRate(),
        createPromotion(),
        createStockCount(),
        createCoverImgFilePath(uuid).getUri(),
        createDetailImgFilePaths(uuid).getUris(),
        createPreviewFilePaths(uuid).getUris(),
        createSampleFilePath(uuid).getUri());
  }
}
