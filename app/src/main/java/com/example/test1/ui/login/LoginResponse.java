package com.example.test1.ui.login;

public class LoginResponse {
    private Long id;
    private String email;
    private String password; // facultatif si tu ne l’affiches pas

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
