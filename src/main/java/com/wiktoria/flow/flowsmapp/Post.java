package com.wiktoria.flow.flowsmapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Post extends Content {
    private List<Comment> comments;

    public Post(User author, String textBody) {
        super(author, textBody);
        this.comments = new ArrayList<>();
    }

    public Post(UUID id, User author, String textBody, Date createdAt) {
        super(id, author, textBody, createdAt);
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public List<Comment> getComments() { return comments; }

    @Override
    public String getType() { return "POST"; }
}