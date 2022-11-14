package com.example.dart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ForgotPwd extends AppCompatActivity {

    private static final String TAG = "Creation compte"; // Pour des tags d'erreurs / verifs

    EditText EdT_email;
    Button btn_send;
    ImageButton Imbtn_back;
    private ForgotPwd activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        // Affectation des EditText
        EdT_email = (EditText) findViewById(R.id.id_email_zone_FP);
        // Affectation des boutons
        btn_send = (Button) findViewById(R.id.id_btn_send_FP);
        // Affectation ImageButton
        Imbtn_back = (ImageButton) findViewById(R.id.id_backButton_FP);

        // Envoie code par email puis passe à la procedure de reinitialisation de mot de passe
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(ForgotPwd.this, Connexion.class);
                startActivity(launchActivity); // a enlever avec l'auth
/*
                AlertDialog.Builder popupEmail = new AlertDialog.Builder(activity);
                popupEmail.setTitle("Reinitialisation du mot de passe");
                popupEmail.setMessage(R.string.resetEmail);
                popupEmail.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        startActivity(launchActivity);
                        mAuth.sendPasswordResetEmail(EdT_email.getText().toString());
                        Toast.makeText(ForgotPwd.this,"Email envoyé", Toast.LENGTH_SHORT).show();
                    }
                });
                popupEmail.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                popupEmail.show();*/
            }
        });


        // Image Bouton retour : retour a la page precedente
        Imbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}