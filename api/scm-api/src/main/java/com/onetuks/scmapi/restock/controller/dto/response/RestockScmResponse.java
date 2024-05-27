package com.onetuks.scmapi.restock.controller.dto.response;

import com.onetuks.coredomain.restock.model.Restock;
import org.springframework.data.domain.Page;

public record RestockScmResponse(long restockId, long bookId, String title, long restockCount) {

  public static RestockScmResponse from(Restock restock) {
    return new RestockScmResponse(
        restock.restockId(),
        restock.book().bookId(),
        restock.book().bookConceptualInfo().title(),
        restock.book().bookStatics().restockCount());
  }

  public record RestockScmResponses(Page<RestockScmResponse> responses) {

    public static RestockScmResponses from(Page<Restock> results) {
      return new RestockScmResponses(results.map(RestockScmResponse::from));
    }
  }
}
