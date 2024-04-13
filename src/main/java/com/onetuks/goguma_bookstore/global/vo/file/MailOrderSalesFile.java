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
public class MailOrderSalesFile extends CustomFile {

  @Column(name = "mail_order_sales_uri")
  private String mailOrderSalesUri;

  public MailOrderSalesFile(String mailOrderSalesUri, MultipartFile multipartFile) {
    super(mailOrderSalesUri, multipartFile);
    this.mailOrderSalesUri = mailOrderSalesUri;
  }

  public String getMailOrderSalesUrl() {
    return getBucketUrl() + getMailOrderSalesUri();
  }
}
