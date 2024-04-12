package com.onetuks.goguma_bookstore.global.vo.file;

import com.onetuks.goguma_bookstore.global.service.vo.File;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MailOrderSales extends File {

  @Column(name = "mail_order_sales_uri")
  private String mailOrderSalesUri;

  public MailOrderSales(String mailOrderSalesUri) {
    this.mailOrderSalesUri = mailOrderSalesUri;
  }

  public String getMailOrderSalesUrl() {
    return getBucketUrl() + getMailOrderSalesUri();
  }
}
