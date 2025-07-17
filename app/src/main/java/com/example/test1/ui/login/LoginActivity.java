package com.example.test1.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔐 Redirection si déjà connecté
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedEmail = prefs.getString("email", null);
        if (savedEmail != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        // Masquer la toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // ⚙️ Configuration Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/") // localhost pour émulateur Android
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            LoginRequest request = new LoginRequest(email, password);

            // 📡 Appel à l'API
            apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // ✅ Connexion réussie → enregistrer et rediriger
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", response.body().getEmail());
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (response.code() == 401) {
                        // ❌ Erreur 401 → mauvais identifiants
                        Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Erreur serveur : " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
