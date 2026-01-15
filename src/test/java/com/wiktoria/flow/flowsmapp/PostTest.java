package com.wiktoria.flow.flowsmapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void testPostCreation() {
        User author = new RegularUser("a@a.pl", "Autor", "1");
        Post post = author.createPost("Testowy wpis");

        assertNotNull(post);
        assertEquals("Testowy wpis", post.getTextBody());
        assertEquals("Autor", post.getAuthor().getUsername());
        assertEquals("POST", post.getType());
    }

    @Test
    void testLikeLogic() {
        User fan = new RegularUser("f@f.pl", "Fan", "1");
        User author = new RegularUser("a@a.pl", "Autor", "1");
        Post post = new Post(author, "Hej");

        post.addLike(fan);
        assertEquals(1, post.getLikeCount());
        assertTrue(post.hasLiked(fan));

        post.removeLike(fan);
        assertEquals(0, post.getLikeCount());
        assertFalse(post.hasLiked(fan));
    }
}