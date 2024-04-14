package com.onetuks.goguma_bookstore.global.vo.file;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EscrowServiceFile extends CustomFile {

  @Column(name = "escrow_service_uri")
  private String escrowServiceUri;

  public EscrowServiceFile(String escrowServiceUri, MultipartFile multipartFile) {
    super(escrowServiceUri, multipartFile);
    this.escrowServiceUri = escrowServiceUri;
  }

  public String getEscrowServiceUrl() {
    return getBucketUrl() + getEscrowServiceUri();
  }
}
