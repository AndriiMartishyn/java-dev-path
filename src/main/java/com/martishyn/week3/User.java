package com.martishyn.week3;

import java.util.List;

public class User {

    private final String id;
    private final String name;
    private final String email;
    private final List<String> roles;

    public User(String id, String name,List<String> roles,String email ) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }
}
