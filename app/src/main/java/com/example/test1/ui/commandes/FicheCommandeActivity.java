package com.example.test1.ui.commandes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.test1.api.ApiService;
import com.example.test1.api.RetrofitClient;
import com.example.test1.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FicheCommandeActivity extends AppCompatActivity {

    private ApiService apiService;
    private Long commandeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_commande);

        commandeId = getIntent().getLongExtra("commandeId", -1);
        apiService = RetrofitClient.getInstance().getApi();

        if (commandeId != -1) {
            telechargerEtAfficherFiche(commandeId);
        } else {
            Toast.makeText(this, "Commande invalide", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void telechargerEtAfficherFiche(Long id) {
        apiService.telechargerFiche(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean saved = enregistrerFichierPdf(response.body(), "fiche_commande_" + id + ".pdf");
                    if (saved) {
                        afficherOptions(id);
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur d'enregistrement", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erreur de téléchargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean enregistrerFichierPdf(ResponseBody body, String nomFichier) {
        try {
            File pdfFile = new File(getExternalFilesDir(null), nomFichier);
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(body.bytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void afficherOptions(Long commandeId) {
        File pdfFile = new File(getExternalFilesDir(null), "fiche_commande_" + commandeId + ".pdf");
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fiche générée");
        builder.setMessage("Que souhaitez-vous faire ?");
        builder.setPositiveButton("📄 Ouvrir", (dialog, which) -> ouvrirFichierPdf(uri));
        builder.setNegativeButton("📤 Partager", (dialog, which) -> partagerFichierPdf(uri));
        builder.setNeutralButton("❌ Fermer", (dialog, which) -> finish());
        builder.show();
    }

    private void ouvrirFichierPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Aucune app pour ouvrir le PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void partagerFichierPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Partager avec..."));
    }
}
