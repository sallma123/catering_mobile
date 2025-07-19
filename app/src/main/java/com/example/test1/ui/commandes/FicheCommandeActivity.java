package com.example.test1.ui.commandes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.test1.R;
import com.example.test1.api.ApiService;
import com.example.test1.api.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

        if (commandeId == -1) {
            Toast.makeText(this, "❌ ID de commande manquant", Toast.LENGTH_LONG).show();
            finish();
        } else {
            telechargerEtAfficherFiche(commandeId);
        }
    }

    private void telechargerEtAfficherFiche(Long id) {
        apiService.telechargerFiche(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ✅ Lire et enregistrer le fichier PDF dans un thread séparé
                    new Thread(() -> {
                        boolean saved = enregistrerFichierPdf(response.body(), "fiche_commande_" + id + ".pdf");
                        runOnUiThread(() -> {
                            if (saved) {
                                afficherOptions(id);
                            } else {
                                showError("❌ Échec d'enregistrement du PDF");
                            }
                        });
                    }).start();
                } else {
                    showError("❌ Réponse invalide lors du téléchargement (code " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showError("❌ Erreur réseau : " + t.getMessage());
            }
        });
    }

    private boolean enregistrerFichierPdf(ResponseBody body, String nomFichier) {
        try {
            File pdfFile = new File(getExternalFilesDir(null), nomFichier);
            InputStream inputStream = body.byteStream();
            FileOutputStream fos = new FileOutputStream(pdfFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
            fos.close();
            inputStream.close();
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
        builder.setTitle("✅ Fiche PDF générée");
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
            Toast.makeText(this, "❌ Aucune application trouvée pour lire les PDF", Toast.LENGTH_LONG).show();
        }
    }

    private void partagerFichierPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Partager avec..."));
    }

    private void showError(String message) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Erreur")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show());
    }
}
