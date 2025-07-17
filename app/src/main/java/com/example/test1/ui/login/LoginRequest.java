package com.example.test1.ui.login;
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // getters & setters (facultatif pour Retrofit)
}
