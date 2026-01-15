package com.wiktoria.flow.flowsmapp;

public class Admin extends User {
    public Admin(String email, String username, String password) {
        super(email, username, password);
    }

    @Override
    public String getType() { return "ADMIN"; }
}