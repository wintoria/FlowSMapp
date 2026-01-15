package com.wiktoria.flow.flowsmapp;

import java.util.Date;
import java.util.UUID;

public class Comment extends Content {
    private UUID parentPostId;

    public Comment(User author, String textBody, Post parentPost) {
        super(author, textBody);
        this.parentPostId = parentPost.getId();
    }

    public Comment(UUID id, User author, String textBody, Date createdAt, UUID parentPostId) {
        super(id, author, textBody, createdAt);
        this.parentPostId = parentPostId;
    }

    public UUID getParentPostId() { return parentPostId; }

    @Override
    public String getType() { return "COMMENT"; }
}