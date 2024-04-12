package com.onetuks.goguma_bookstore.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DefaultAddressInfo {

  @Column(name = "default_address")
  private String defaultAddress;

  @Column(name = "default_address_detail")
  private String defaultAddressDetail;

  @Builder
  public DefaultAddressInfo(String defaultAddress, String defaultAddressDetail) {
    this.defaultAddress = defaultAddress;
    this.defaultAddressDetail = defaultAddressDetail;
  }
}
