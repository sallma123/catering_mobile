package com.example.test1.ui.commandes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test1.api.ApiService;
import com.example.test1.api.RetrofitClient;
import com.example.test1.model.Commande;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandesViewModel extends ViewModel {

    private final MutableLiveData<List<Commande>> commandesLiveData = new MutableLiveData<>();

    public CommandesViewModel() {
        chargerCommandesDepuisApi();
    }

    public LiveData<List<Commande>> getCommandes() {
        return commandesLiveData;
    }

    private void chargerCommandesDepuisApi() {
        ApiService apiService = RetrofitClient.getInstance().getApi();
        Call<List<Commande>> call = apiService.getCommandes();

        Log.d("API_CALL", "📡 Lancement de l'appel vers /api/commandes...");

        call.enqueue(new Callback<List<Commande>>() {
            @Override
            public void onResponse(Call<List<Commande>> call, Response<List<Commande>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_SUCCESS", "✅ " + response.body().size() + " commandes reçues");
                    commandesLiveData.setValue(response.body());
                } else {
                    Log.e("API_FAIL", "❌ Réponse vide ou erreur : " + response.code());
                    commandesLiveData.setValue(new ArrayList<>()); // éviter le null
                }
            }

            @Override
            public void onFailure(Call<List<Commande>> call, Throwable t) {
                Log.e("API_ERROR", "❌ Erreur réseau ou serveur : " + t.getMessage());
                commandesLiveData.setValue(new ArrayList<>()); // éviter null pour le fragment
            }
        });
    }
}
