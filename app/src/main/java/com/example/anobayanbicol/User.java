package com.example.anobayanbicol;

public class User {
    public String name;
    public String email;
    public String password;

    public User() {
        // Required for Firebase
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
