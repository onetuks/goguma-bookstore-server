package com.onetuks.coredomain.book.model;

import com.onetuks.coredomain.member.model.Member;

public record Comment(
    Long commentId,
    Book book,
    Member member,
    String title,
    String content
) {

}
