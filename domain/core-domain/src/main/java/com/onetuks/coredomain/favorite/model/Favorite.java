package com.onetuks.coredomain.favorite.model;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.member.model.Member;

public record Favorite(Long favoriteId, Member member, Book book) {}
