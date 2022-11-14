package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Parametres extends AppCompatActivity {

    private static final String TAG = "Connexion"; // Pour des tags d'erreurs / verifs

    Button btn_deconnexion, btn_supprCompte;
    ImageButton Imbtn_stylo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Imbtn_stylo = (ImageButton) findViewById(R.id.id_img_stylo_P);
        btn_deconnexion = (Button) findViewById(R.id.id_btn_decoAccount_P);
        btn_supprCompte = (Button) findViewById(R.id.id_btn_delAccount_P);

        // Bouton modif profil (icone stylo) : changement icone
        Imbtn_stylo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Parametres.this, Parametres.class);
                startActivity(launchActivity);
            }
        });

        // Bouton deconnexion : deconnexion puis redirection page de connexion
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Parametres.this, Connexion.class);
                startActivity(launchActivity);
            }
        });

        // Bouton suppression compte : suppression puis redirection vers page de connexion
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Parametres.this, Connexion.class);
                startActivity(launchActivity);
            }
        });


        // Initialisation bottom navigation :
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // On met Conversion en selectionne par defaut:
        bottomNavigationView.setSelectedItemId(R.id.nav_parametres);

        // Mise en place du Listenner:
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu:
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_historique:
                        return true;

                    case R.id.nav_statistiques:
                        startActivity(new Intent(getApplicationContext(), Statistiques.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_parametres:
                        startActivity(new Intent(getApplicationContext(), Parametres.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }


}
