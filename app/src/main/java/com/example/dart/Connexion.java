package com.example.dart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Connexion extends AppCompatActivity {

    private static final String TAG = "Connexion"; // Pour des tags d'erreurs / verifs

    EditText EdT_email, EdT_password;
    Button btn_newAccount, btn_forgotPwd, btn_connexion;
    ImageButton Imbtn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Affectation des EditText
        EdT_email = (EditText) findViewById(R.id.id_email_zone);
        EdT_password = (EditText) findViewById(R.id.id_pwd_zone);
        // Affectation des boutons
        btn_newAccount = (Button) findViewById(R.id.id_btn_newAccount);
        btn_forgotPwd = (Button) findViewById(R.id.id_btn_forgotPwd);
        btn_connexion = (Button) findViewById(R.id.id_btn_connexion);
        // Affectation ImageButton
        Imbtn_back = (ImageButton) findViewById(R.id.id_backButton);

        // Bouton connexion : ouverture du menu
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // ligne en dessous à commenter/decommenter pr se passer de l'authentification (pour tests)
                Intent launchActivity = new Intent(Connexion.this, Connexion.class);
                startActivity(launchActivity);
                try {
                    //signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
                catch (Exception e){
                    //reload();
                }
            }
        });

        // Bouton pas encore de compte : Ouverture page Creation compte
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Connexion.this, Connexion.class);
                startActivity(launchActivity);
                finish();
            }
        });

        // Bouton mdp oublie : Ouverture page Mot de passe oublie
        btn_forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Connexion.this, Connexion.class);
                startActivity(launchActivity);
                finish();
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