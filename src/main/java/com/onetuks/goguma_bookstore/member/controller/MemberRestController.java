package com.onetuks.goguma_bookstore.member.controller;

import com.onetuks.goguma_bookstore.member.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/members")
public class MemberRestController {

  private final MemberService memberService;

  public MemberRestController(MemberService memberService) {
    this.memberService = memberService;
  }
}
