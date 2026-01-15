package com.wiktoria.flow.flowsmapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class Content implements Likeable {
    protected UUID id;
    protected String textBody;
    protected Date createdAt;
    protected User author;
    protected List<String> likes;

    public Content(User author, String textBody) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.textBody = textBody;
        this.createdAt = new Date();
        this.likes = new ArrayList<>();
    }

    public Content(UUID id, User author, String textBody, Date createdAt) {
        this.id = id;
        this.author = author;
        this.textBody = textBody;
        this.createdAt = createdAt;
        this.likes = new ArrayList<>();
    }

    public void edit(String newText) {
        this.textBody = newText;
    }

    @Override
    public void addLike(User user) {
        if (!likes.contains(user.getUsername())) {
            likes.add(user.getUsername());
        }
    }

    @Override
    public void removeLike(User user) {
        likes.remove(user.getUsername());
    }

    @Override
    public boolean hasLiked(User user) {
        return likes.contains(user.getUsername());
    }

    @Override
    public int getLikeCount() {
        return likes.size();
    }

    public String getTextBody() { return textBody; }
    public User getAuthor() { return author; }
    public Date getCreatedAt() { return createdAt; }
    public UUID getId() { return id; }

    public abstract String getType();
}