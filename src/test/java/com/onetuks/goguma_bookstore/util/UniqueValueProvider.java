package com.onetuks.goguma_bookstore.util;

import java.util.Random;

public class UniqueValueProvider {

  private static final Random random = new Random();

  public static String createBusinessNumber() {
    return String.valueOf(random.nextLong(1_000_000_000L, 9_999_999_999L));
  }

  public static String createMailOrderSalesNumber() {
    return String.valueOf(random.nextLong(100_000_000_000_000_000L, 999_999_999_999_999_999L));
  }

  public static String createIsbn() {
    return String.valueOf(random.nextLong(1_000_000_000_000L, 9_999_999_999_999L));
  }
}
