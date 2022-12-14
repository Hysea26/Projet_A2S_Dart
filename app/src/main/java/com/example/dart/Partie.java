package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class Partie extends AppCompatActivity {

    private static final String TAG = "Partie";


    // Declaration variables action partie
    private EditText lance1;
    private EditText lance2;
    private EditText lance3;
    private TextView PointTour;
    private TextView PointRestantTemporaire;
    private TextView PointRestantRV;
    private TextView LegCompteur;
    private TextView SetCompteur;
    private Button boutonValide;

    // Declaration variables Recycler view
    private ArrayList<RowItemPartie> mPartieList;
    private RecyclerView recyclerViewPartie;
    private MyAdapterPartie mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> strPseudoJoueurs = new ArrayList<String>();
    ArrayList<String> strIdJoueurs = new ArrayList<String>();
    ArrayList<Integer> point = new ArrayList<Integer>();

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Variables laetitia
    private int ChoixScore;
    private int ChoixLeg;
    private int ChoixSet;
    private ArrayList<Joueurs> listeJoueurs;
    private ArrayList<String> listePseudos = new ArrayList<>();
    private ArrayList<Integer> listeScores = new ArrayList<>();
    private ArrayList<Integer> listeLegs = new ArrayList<>();
    private ArrayList<Integer> listeSets = new ArrayList<>();
    private ArrayList<Integer> listeRounds = new ArrayList<>();
    private String IdPartie;
    int positionPartie = 0;
    ArrayList<Parties> listeParties = new ArrayList<>();
    private TextView TV_ScoreBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // variables laetitia
        positionPartie = getIntent().getIntExtra("position", 0);     // recupere la valeur de la position de la partie dans firebase
        TV_ScoreBoard = findViewById(R.id.topScoreBoard);

        RecupJoueursPartie(positionPartie);

        lance1 = findViewById(R.id.edittext_lance_1);
        lance2 = findViewById(R.id.edittext_lance_2);
        lance3 = findViewById(R.id.edittext_lance_3);
        PointTour = findViewById(R.id.resultat);
        PointRestantTemporaire = findViewById(R.id.PointRestantTemporaire);
        boutonValide = findViewById(R.id.boutonValide);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int temp1 = 0;
                int temp2 = 0;
                int temp3 = 0;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!lance1.getText().toString().equals("") && !lance2.getText().toString().equals("") && !lance3.getText().toString().equals("")) {
                    int temp1 = Integer.parseInt(lance1.getText().toString());
                    int temp2 = Integer.parseInt(lance2.getText().toString());
                    int temp3 = Integer.parseInt(lance3.getText().toString());
                    PointTour.setText(String.valueOf(temp1 + temp2 + temp3));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }

        };
        lance1.addTextChangedListener(textWatcher);
        lance2.addTextChangedListener(textWatcher);
        lance3.addTextChangedListener(textWatcher);

        // Clique boutton validé
        boutonValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PointRestantTemporaire = PointRestantTemporaire - PointTour
                if (!lance1.getText().toString().equals("") && !lance2.getText().toString().equals("") && !lance3.getText().toString().equals("")) {
                    int pr = Integer.parseInt(PointRestantTemporaire.getText().toString());
                    int pt = Integer.parseInt(PointTour.getText().toString());
                    PointRestantTemporaire.setText(String.valueOf(pr - pt));
                    lance1.setText(null);
                    lance2.setText(null);
                    lance3.setText(null);

                }
                else {
                    Toast.makeText(Partie.this, "Il manque un lancé", Toast.LENGTH_SHORT).show();
                }

            }
        });



} //////////////////////////////// Fin OnCreate ///////////////////////////////////////


    public void RecupJoueursPartie(int position) { // recup des joueurs deja crees
        db.collection("Parties")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture", "Entre dans le oncomplete : ");
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Parties> downloadInfoList = task.getResult().toObjects(Parties.class); // Va chercher dans joueurs heritant users

                                for (int i = 0; i < downloadInfoList.size(); i++) {
                                    listeParties.add(downloadInfoList.get(i));
                                }


                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }
                            RecupVariablesBDD();// Recup des variables de la BDD (score, leg, set...)
                            createPartieList(); // Cree la liste de joueurs presents dans la BDD
                            buildRecyclerView(); // Construit le recycler view
                        }
                    }
                });
    }

    public void RecupVariablesBDD(){

        IdPartie = listeParties.get(positionPartie).getIdPartie();
        Log.d("Waouh", "IdPartie : "+ IdPartie);

        // Recup du choix de leg et set de l'utilisateur (en combien de set / leg la partie va se jouer)
        ChoixSet = listeParties.get(positionPartie).getChoixSet();
        ChoixLeg = listeParties.get(positionPartie).getChoixLeg();
        ChoixScore = listeParties.get(positionPartie).getChoixScore();

        // Recup de la liste des joueurs participant et recup de leurs pseudos
        listeJoueurs = listeParties.get(positionPartie).getJoueursChecked();
        for (int j=0; j < listeJoueurs.size(); j++){
            listePseudos.add(listeJoueurs.get(j).getPseudo());
        }

        // Recup des scores des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeScores = listeParties.get(positionPartie).getScores();
        if (listeScores.isEmpty()){ // Si liste vide : cas de creation de partie, affectation du choixScore
            InitialisationScore(listeScores,listeJoueurs,ChoixScore);
            db.collection("Parties").document(IdPartie).update("scores", listeScores); // Mise à jour des donnees (score) dans la BDD
        }

        // Recup des legs gagnes des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeLegs = listeParties.get(positionPartie).getLegs();
        if (listeLegs.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationScore(listeLegs,listeJoueurs,0);
            db.collection("Parties").document(IdPartie).update("legs", listeLegs); // Mise à jour des donnees (legs) dans la BDD
        }

        // Recup des legs gagnes des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeSets = listeParties.get(positionPartie).getSets();
        if (listeSets.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationScore(listeSets,listeJoueurs,0);
            db.collection("Parties").document(IdPartie).update("sets", listeSets); // Mise à jour des donnees (sets) dans la BDD
        }

        // Recup des rounds des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeRounds = listeParties.get(positionPartie).getRounds();
        if (listeRounds.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationScore(listeRounds,listeJoueurs,0);
            db.collection("Parties").document(IdPartie).update("rounds", listeRounds); // Mise à jour des donnees (sets) dans la BDD
        }

    }

    public void InitialisationScore(ArrayList<Integer> listeInteger, ArrayList<Joueurs> listeSize, int choixPts){

        // Ajout de choixPts aux listes integer (score, leg, set)
        for (int i=0; i<listeSize.size(); i++){
            listeInteger.add(choixPts);
        }

    }

    public void createPartieList() {

        // Affichage du choixLeg et choixSet (partie en X set(s), Y legs)
        TV_ScoreBoard.setText("En "+ChoixSet+ " Set(s), "+ChoixLeg+" Legs");

        mPartieList = new ArrayList<>();

        for (int i = 0; i < listeJoueurs.size(); i++) {
            mPartieList.add(new RowItemPartie(R.drawable.img_user_profil, listePseudos.get(i),listeSets.get(i), listeLegs.get(i), listeScores.get(i), listeRounds.get(i)));
        }

        Log.d("Waouh", "mPartieList :" + mPartieList);
    }

    public void buildRecyclerView() {

        recyclerViewPartie = findViewById(R.id.recyclerViewPartie);

        mAdapter = new MyAdapterPartie(mPartieList); // cree avec mPartieList
        recyclerViewPartie.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPartie.setLayoutManager(mLayoutManager);

        recyclerViewPartie.setHasFixedSize(true);


    }




}