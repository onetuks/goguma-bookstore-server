package com.onetuks.goguma_bookstore.author.model.vo;

import com.onetuks.goguma_bookstore.global.service.vo.File;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EscrowService extends File {

  @Column(name = "escrow_service_uri")
  private String escrowServiceUri;

  public EscrowService(String escrowServiceUri) {
    this.escrowServiceUri = escrowServiceUri;
  }

  public String getEscrowServiceUrl() {
    return getBucketUrl() + getEscrowServiceUri();
  }
}
