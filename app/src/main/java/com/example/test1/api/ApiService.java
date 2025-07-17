package com.example.test1.api;

import com.example.test1.ui.login.LoginRequest;
import com.example.test1.ui.login.LoginResponse;
import com.example.test1.ui.register.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<Void> registerUser(@Body RegisterRequest registerRequest);
}
