package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


//////////////////// Selyan /////////////////////

public class Statistiques extends AppCompatActivity {
    String parametres[]={"Parties jouées", "Mètres parcourus par les fléchettes", "Dernier meilleur score", "Nombre d'amis total", "Nombre de Legs gagnés", "Nombre de sets gagnés", "Nombre de parties gagnées" };
    int Statistiques_Images [] = {R.drawable.img_statistiques, R.drawable.img_statistiques, R.drawable.img_statistiques, R.drawable.img_statistiques, R.drawable.img_statistiques, R.drawable.img_statistiques, R.drawable.img_statistiques};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);


        //Initialisation:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //On met Conversion en selectionne par défaut:
        bottomNavigationView.setSelectedItemId(R.id.nav_statistiques);

        //Mise en place du Listenner:
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
