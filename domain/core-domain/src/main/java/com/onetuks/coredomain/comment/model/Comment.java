package com.onetuks.coredomain.comment.model;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.member.model.Member;

public record Comment(Long commentId, Book book, Member member, String title, String content) {}
