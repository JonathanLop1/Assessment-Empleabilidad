package com.assessment.projectmanagement.domain.model;

import java.util.UUID;

/**
 * Domain entity representing a User
 * Pure Java class with no framework dependencies
 */
public class User {
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;

    public User(UUID id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
