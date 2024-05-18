package com.onetuks.scmapi.registration.dto.response;

import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.enums.book.Category;
import java.util.List;
import org.springframework.data.domain.Page;

public record RegistrationResponse(
    long registrationId,
    boolean approvalResult,
    String approvalMemo,
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String publisher,
    String isbn,
    int height,
    int width,
    String coverType,
    long pageCount,
    long price,
    int salesRate,
    boolean isPromotion,
    long stockCount,
    String coverImgUrl,
    List<String> detailImgUrls,
    List<String> previewUrls,
    String sampleUrl) {

  public static RegistrationResponse from(Registration registration) {
    return new RegistrationResponse(
        registration.registrationId(),
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
        registration.coverImgFilePath().getUrl(),
        registration.detailImgFilePaths().getUrls(),
        registration.previewFilePaths().getUrls(),
        registration.sampleFilePath().getUrl()
    );
  }

  public record RegistrationResponses(Page<RegistrationResponse> responses) {

    public static RegistrationResponses from(Page<Registration> results) {
      return new RegistrationResponses(results.map(RegistrationResponse::from));
    }
  }
}
