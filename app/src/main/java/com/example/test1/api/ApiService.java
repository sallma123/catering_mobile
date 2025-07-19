package com.example.test1.api;

import com.example.test1.model.Commande;
import com.example.test1.model.CommandeDTO;
import com.example.test1.ui.login.LoginRequest;
import com.example.test1.ui.login.LoginResponse;
import com.example.test1.ui.register.RegisterRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<Void> registerUser(@Body RegisterRequest registerRequest);

    // ✅ Ajouter cet endpoint pour récupérer les commandes
    @GET("/api/commandes")
    Call<List<Commande>> getCommandes();
    @Streaming
    @GET("/api/commandes/{id}/fiche")
    Call<ResponseBody> telechargerFiche(@Path("id") Long commandeId);
    @POST("/api/commandes")
    Call<Commande> creerCommande(@Body CommandeDTO commandeDTO);

}
