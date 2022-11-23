package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class Menu extends AppCompatActivity {

    private Button NouvellePartieBtn;

    // Declaration variables Recycler view
    private ArrayList<RowItemJoueur> mExampleList;
    private RecyclerView mrvArticles;
    private MyAdapterJoueur mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> strTtlArticles = new ArrayList<String>();
    ArrayList<String> strSumArticles = new ArrayList<String>();
    ArrayList<String> strPartieRobotArticles = new ArrayList<String>();
    ArrayList<String> strIdArticles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        NouvellePartieBtn = (Button) findViewById(R.id.NouvellePartieBtn);

        NouvellePartieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Partie.class);
                startActivity(intent);
            }
        });

        //Initialisationrtjth:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //On met Conversion en selectionne par défaut:
        bottomNavigationView.setSelectedItemId(R.id.nav_menu);

        //Mise en place du Listenner:
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_menu:
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_historique:
                        startActivity(new Intent(getApplicationContext(), Historique.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_statistiques:
                        return true;

                    case R.id.nav_parametres:
                        startActivity(new Intent(getApplicationContext(), Parametres.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    public void insertItem(int position) {
        mExampleList.add(position, new RowItemJoueur(R.drawable.img_user_profil, "Ajout" + position, "This is Line 2"));
        mAdapter.notifyItemInserted(position);
    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeNbParties(text);
        mAdapter.notifyItemChanged(position);
    }

}