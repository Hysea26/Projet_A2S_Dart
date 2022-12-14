package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Historique extends AppCompatActivity {

    private static final String TAG = "Historique";

    // Declaration variables Recycler view
    private ArrayList<RowItemPEC> mExampleList;
    private RecyclerView mrvArticles;
    private MyAdapterPEC mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> strPseudoJoueurs = new ArrayList<String>();
    ArrayList<ArrayList<Joueurs>> listeJoueursPartie = new ArrayList<>();
    ArrayList<ArrayList<Integer>> listeScoresPartie = new ArrayList<>();
    ArrayList<ArrayList<Boolean>> listeBooleanPartie = new ArrayList<>();
    ArrayList<ArrayList<Integer>> listeRoundsPartie = new ArrayList<>();

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Affichage des parties en cours du joueur dans le recycler view
        RecupParties();


        //Initialisation:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //On met Conversion en selectionne par défaut:
        bottomNavigationView.setSelectedItemId(R.id.nav_historique);

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


    public void RecupParties(){ // recup des joueurs deja crees pour affichage dans recycler view menu
        db.collection("Parties")
                //.orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture RecupParties", "Entre dans le oncomplete : ");

                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Parties> downloadInfoList = task.getResult().toObjects(Parties.class); // Va chercher dans joueurs heritant users

                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    listeJoueursPartie.add(downloadInfoList.get(i).getJoueursChecked());
                                    listeScoresPartie.add(downloadInfoList.get(i).getScores());
                                    listeBooleanPartie.add(downloadInfoList.get(i).getBooleanPartieEnCours());
                                    listeRoundsPartie.add(downloadInfoList.get(i).getRounds());
                                }

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }
                            createExampleList(); // Cree la liste de joueurs presents dans la BDD
                            buildRecyclerView(); // Construit le recycler view
                        }
                    }
                });
    }

    public void createExampleList() {

        mExampleList = new ArrayList<>();

        // Initialisation variables pour recup pseudos de la partie
        ArrayList<Joueurs> listeJoueursDeLaPartie = new ArrayList<>();
        ArrayList<String> pseudosJoueursDeLaPartie = new ArrayList<>();

        // Initialisation variables pour recup scores de la partie
        String sLegende = "Scores : ";
        ArrayList<Integer> listeS = new ArrayList<>();

        // Initialisation variables pour recup boolean fin de partie
        ArrayList<Boolean> listeBoolean = new ArrayList<>();
        ArrayList<Integer> listeRounds = new ArrayList<>();

        // boucle parcourant la liste de joueurs de toutes les parties (liste de liste)
        for (int i=0; i<listeJoueursPartie.size();i++) {
            listeJoueursDeLaPartie = listeJoueursPartie.get(i); // recup des joueurs de la partie i

            pseudosJoueursDeLaPartie.clear();   // Vide la liste pour reutilisation
            String strPseudosJ = "";            // reset de la chaine de caractere

            listeS = listeScoresPartie.get(i);
            sLegende = "Scores : ";

            listeBoolean = listeBooleanPartie.get(i);
            listeRounds = listeRoundsPartie.get(i);

            // boucle parcourant la liste des joueurs de la partie i
            for (int j=0; j<listeJoueursDeLaPartie.size(); j++){
                pseudosJoueursDeLaPartie.add(listeJoueursDeLaPartie.get(j).getPseudo()); // recup des pseudos associes aux joueurs

                // boucle conditionnelle pour le string s'affichant dans le recycler view (pseudos des joueurs de la partie)
                if (j < listeJoueursDeLaPartie.size()-1){
                    strPseudosJ += pseudosJoueursDeLaPartie.get(j).toString() + ", ";
                } else {
                    strPseudosJ += pseudosJoueursDeLaPartie.get(j).toString();
                }
            }

            for (int l=0; l<listeS.size(); l++){
                if (booleanFinPartie(listeBoolean)) { // si la partie est encore en cours pour tous les joueurs
                    if (l < listeS.size() - 1) {
                        sLegende += listeS.get(l).toString() + ",";
                    } else {
                        sLegende += listeS.get(l).toString();
                    }
                } else { // si au moins l'un des joueurs a termine la partie
                    int pos = CalculResultats(listeRounds,pseudosJoueursDeLaPartie);
                    sLegende = "Partie terminee, vainqueur : " + listeJoueursDeLaPartie.get(pos).getPseudo();
                }

            }

            // Ajout de chaque partie en cours dans le recycler view
            mExampleList.add(new RowItemPEC(R.drawable.img_user_profil,strPseudosJ,sLegende));
        }


    }

    public int CalculResultats(ArrayList<Integer> listeR, ArrayList<String> listeP){
        int positionVainqueur = 0;
        int roundVainqueur = listeR.get(0);
        for (int i=1; i<listeR.size(); i++){
            if (listeR.get(i) > roundVainqueur){
                roundVainqueur = listeR.get(i);
                positionVainqueur = i;
            }
        }
        return positionVainqueur;
    }

    public boolean booleanFinPartie(ArrayList<Boolean> listeB){
        for (int i=0; i<listeB.size(); i++){
            if (!listeB.get(i)){
                return false;
            }
        }
        return true;
    }

    public void buildRecyclerView() {
        mrvArticles = findViewById(R.id.id_RV_PEC);

        mAdapter = new MyAdapterPEC(mExampleList); // cree avec mExampleList, a changer
        mrvArticles.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mrvArticles.setLayoutManager(mLayoutManager);

        mrvArticles.setHasFixedSize(true);

        mAdapter.setOnItemClickListener(new MyAdapterPEC.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { // si img couleur clique, change texte

                Intent launchActivity = new Intent(Historique.this, Partie.class);
                launchActivity.putExtra("position", position);   // transmet la valeur de position à Partie (pour recup donnees en position i)
                startActivity(launchActivity);

            }

            @Override
            public void onDeleteClick(int position) { // si item noir clique, supprime
                //removeItem(position);
            }
        });
    }



}
