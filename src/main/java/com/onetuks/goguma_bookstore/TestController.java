package com.onetuks.goguma_bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping(path = "/")
  public String home() {
    return "home";
  }
}
