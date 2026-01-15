package com.wiktoria.flow.flowsmapp;

public interface Likeable {
    void addLike(User user);
    void removeLike(User user);
    int getLikeCount();
    boolean hasLiked(User user);
}