package com.onetuks.coredomain.subscribe.model;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.member.model.Member;

public record Subscribe(Long subscribeId, Member member, Author author) {}
