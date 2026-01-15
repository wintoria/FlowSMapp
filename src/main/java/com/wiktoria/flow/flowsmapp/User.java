package com.wiktoria.flow.flowsmapp;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String username;
    protected String email;
    protected String password;
    protected Profile profile;
    protected List<String> following;
    protected List<String> followers;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profile = new Profile();
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

    public boolean login(String login, String pass) {
        return (this.username.equals(login) || this.email.equals(login)) && this.password.equals(pass);
    }

    public Post createPost(String content) {
        return new Post(this, content);
    }

    // Logika obserwowania
    public void follow(User userToFollow) {
        if (!following.contains(userToFollow.getUsername())) {
            this.following.add(userToFollow.getUsername());
            userToFollow.addFollower(this.username);
        }
    }

    public void unfollow(User userToUnfollow) {
        this.following.remove(userToUnfollow.getUsername());
        userToUnfollow.removeFollower(this.username);
    }

    public void addFollower(String username) {
        if (!followers.contains(username)) followers.add(username);
    }

    public void removeFollower(String username) {
        followers.remove(username);
    }

    public boolean isFollowing(String username) {
        return following.contains(username);
    }

    // Gettery
    public Profile getProfile() { return this.profile; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public List<String> getFollowing() { return following; }
    public List<String> getFollowers() { return followers; }

    public abstract String getType();
}