package com.example.test1.ui.commandes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test1.api.ApiService;
import com.example.test1.api.RetrofitClient;
import com.example.test1.model.Commande;

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

        call.enqueue(new Callback<List<Commande>>() {
            @Override
            public void onResponse(Call<List<Commande>> call, Response<List<Commande>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commandesLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Commande>> call, Throwable t) {
                // Tu peux ajouter un Toast ou une log si tu veux
                commandesLiveData.setValue(null);
            }
        });
    }
}
