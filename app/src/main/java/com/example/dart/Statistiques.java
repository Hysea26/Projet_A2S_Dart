package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


//////////////////// Selyan /////////////////////

public class Statistiques extends AppCompatActivity {

    // Variables
    private TextView TV1;
    private String ps;
    private String NBPJ;
    private int MLF;
    private int NBA;
    private int NBSG;
    private int NBLG;

    // Firebase
    private FirebaseFirestore db; //On appelle  notre database
    private FirebaseAuth mAuth; //Pour savoir qui est connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        TV1 = findViewById(R.id.id_TV_Stats);

        // Recup stats
        RecupJoueursStats();

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


    public void RecupJoueursStats(){ // recup des joueurs deja crees pour affichage dans recycler view menu
        db.collection("Joueurs")
                .orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture RecupJoueurs", "Entre dans le oncomplete : ");
                        FirebaseUser currentUser = mAuth.getCurrentUser(); // recup du current user

                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Joueurs> downloadInfoList = task.getResult().toObjects(Joueurs.class); // Va chercher dans joueurs heritant users

                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    if (currentUser.getEmail().equals(downloadInfoList.get(i).getEmail())){ // recup que l'utilisateur
                                        ps = downloadInfoList.get(i).getPseudo();
                                        NBPJ = downloadInfoList.get(i).getPseudo();
                                        MLF = downloadInfoList.get(i).getMeilleurLanceFlechette();
                                        NBA = downloadInfoList.get(i).getnbAmis();
                                        NBSG = downloadInfoList.get(i).getnbLegGagnes();
                                        NBLG = downloadInfoList.get(i).getnbSetGagnes();b

                                    }

                                }

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }

                            Affichage_Stats(); /////// fonction affichant les valeurs recup dans la page stats
                        }
                    }
                });
    }

    public void Affichage_Stats(){ //fonction qui permet de mettre nos stats de la db avec nos textes
        TV1.setText("\n\n\n\n\nPseudo : " + ps + "\n\nMeilleur lancer : " + MLF + "\n\nNombre d'amis : " + 3 + "\n\nNombre de sets gagnés : " + NBSG + "\n\nNombre de legs gagnés : " + NBLG);
    }

}
