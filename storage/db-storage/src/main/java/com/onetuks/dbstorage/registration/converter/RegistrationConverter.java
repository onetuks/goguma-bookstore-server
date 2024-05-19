package com.onetuks.dbstorage.registration.converter;

import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.global.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.global.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.global.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.global.file.filepath.SampleFilePath;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.model.vo.ApprovalInfo;
import com.onetuks.dbstorage.author.converter.AuthorConverter;
import com.onetuks.dbstorage.registration.entity.RegistrationEntity;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConverter {

  private final AuthorConverter authorConverter;

  public RegistrationConverter(AuthorConverter authorConverter) {
    this.authorConverter = authorConverter;
  }

  public RegistrationEntity toEntity(Registration registration) {
    return new RegistrationEntity(
        registration.registrationId(),
        authorConverter.toEntity(registration.author()),
        registration.approvalInfo().isApproved(),
        registration.approvalInfo().approvalMemo(),
        registration.bookConceptualInfo().title(),
        registration.bookConceptualInfo().oneLiner(),
        registration.bookConceptualInfo().summary(),
        registration.bookConceptualInfo().categories(),
        registration.bookConceptualInfo().publisher(),
        registration.bookConceptualInfo().isbn(),
        registration.bookPhysicalInfo().height(),
        registration.bookPhysicalInfo().width(),
        registration.bookPhysicalInfo().coverType(),
        registration.bookPhysicalInfo().pageCount(),
        registration.bookPriceInfo().price(),
        registration.bookPriceInfo().salesRate(),
        registration.bookPriceInfo().isPromotion(),
        registration.bookPriceInfo().stockCount(),
        registration.coverImgFilePath().getUri(),
        registration.detailImgFilePaths().getUris(),
        registration.previewFilePaths().getUris(),
        registration.sampleFilePath().getUri());
  }

  public Registration toDomain(RegistrationEntity registrationEntity) {
    return new Registration(
        registrationEntity.getRegistrationId(),
        authorConverter.toDomain(registrationEntity.getAuthorEntity()),
        new ApprovalInfo(registrationEntity.getIsApproved(), registrationEntity.getApprovalMemo()),
        new BookConceptualInfo(
            registrationEntity.getTitle(),
            registrationEntity.getOneLiner(),
            registrationEntity.getSummary(),
            registrationEntity.getCategories(),
            registrationEntity.getPublisher(),
            registrationEntity.getIsbn()),
        new BookPhysicalInfo(
            registrationEntity.getHeight(),
            registrationEntity.getWidth(),
            registrationEntity.getCoverType(),
            registrationEntity.getPageCount()),
        new BookPriceInfo(
            registrationEntity.getPrice(),
            registrationEntity.getSalesRate(),
            registrationEntity.getIsPromotion(),
            registrationEntity.getStockCount()),
        CoverImgFilePath.of(registrationEntity.getCoverImgUri()),
        DetailImgFilePaths.of(registrationEntity.getDetailImgUris()),
        PreviewFilePaths.of(registrationEntity.getPreviewUris()),
        SampleFilePath.of(registrationEntity.getSampleUri()));
  }
}
