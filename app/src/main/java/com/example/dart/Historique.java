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
                        //FirebaseUser currentUser = mAuth.getCurrentUser();
                        //Toast.makeText(Menu.this,"Connexion en tant que \n\t" + currentUser.getEmail(),Toast.LENGTH_SHORT).show();

                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Parties> downloadInfoList = task.getResult().toObjects(Parties.class); // Va chercher dans joueurs heritant users

                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    listeJoueursPartie.add(downloadInfoList.get(i).getJoueursChecked());
                                    listeScoresPartie.add(downloadInfoList.get(i).getScores());

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

        String sLegende = "Scores : ";
        ArrayList<Integer> listeS = new ArrayList<>();

        // boucle parcourant la liste de joueurs de toutes les parties (liste de liste)
        for (int i=0; i<listeJoueursPartie.size();i++) {
            listeJoueursDeLaPartie = listeJoueursPartie.get(i); // recup des joueurs de la partie i

            pseudosJoueursDeLaPartie.clear();   // Vide la liste pour reutilisation
            String strPseudosJ = "";            // reset de la chaine de caractere

            listeS = listeScoresPartie.get(i);
            sLegende = "Scores : ";

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
                if (l<listeS.size()-1){
                    sLegende += listeS.get(l).toString() + ",";
                } else {
                    sLegende += listeS.get(l).toString();
                }
            }

            // Ajout de chaque partie en cours dans le recycler view
            mExampleList.add(new RowItemPEC(R.drawable.img_user_profil,strPseudosJ,sLegende));
        }


        /*
        for(int i=0;i<scoresPartie.size();i++) {
            mExampleList.add(new RowItemPEC(R.drawable.img_user_profil, "strPseudoJoueurs.get(i).toString()", scoresPartie.get(i).toString()));
            //Log.d("Create exemple list Waouh", "mExampleList :"+scoresPartie.get(i));
        }*/
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
