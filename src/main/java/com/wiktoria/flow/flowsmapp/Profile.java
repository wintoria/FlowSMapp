package com.wiktoria.flow.flowsmapp;

public class Profile {
    private String bio;

    public Profile() {
        this.bio = "Nowy użytkownik.";
    }

    public void updateBio(String newBio) {
        this.bio = newBio;
    }

    public String getBio() {
        return this.bio;
    }
}