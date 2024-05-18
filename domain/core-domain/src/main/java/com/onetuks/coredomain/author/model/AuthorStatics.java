package com.onetuks.coredomain.author.model;

public record AuthorStatics(
    Long authorStaticsId,
    long subscribeCount,
    long bookCount,
    long restockCount
) {}
