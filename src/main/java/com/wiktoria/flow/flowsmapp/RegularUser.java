package com.wiktoria.flow.flowsmapp;

public class RegularUser extends User {
    public RegularUser(String email, String username, String password) {
        super(email, username, password);
    }

    @Override
    public String getType() { return "REGULAR"; }
}