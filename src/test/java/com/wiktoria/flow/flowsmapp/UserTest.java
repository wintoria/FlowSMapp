package com.wiktoria.flow.flowsmapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testLoginSuccess() {
        User user = new RegularUser("wiki@test.pl", "wiki", "123");

        assertTrue(user.login("wiki", "123"));
        assertTrue(user.login("wiki@test.pl", "123"));
    }

    @Test
    void testLoginFailure() {
        User user = new RegularUser("wiki@test.pl", "wiki", "123");

        assertFalse(user.login("wiki", "zlehaslo"));
    }

    @Test
    void testFollowSystem() {
        User u1 = new RegularUser("a@a.pl", "UserA", "1");
        User u2 = new RegularUser("b@b.pl", "UserB", "1");

        u1.follow(u2);

        assertTrue(u1.isFollowing("UserB"), "UserA powinien mieć UserB na liście following");
        assertTrue(u2.getFollowers().contains("UserA"), "UserB powinien mieć UserA na liście followers");
    }
}