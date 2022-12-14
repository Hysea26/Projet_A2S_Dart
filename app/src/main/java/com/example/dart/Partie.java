package com.example.dart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;



public class Partie extends AppCompatActivity {

    private static final String TAG = "Partie";

    // Declaration variables action partie
    private EditText lance1;
    private EditText lance2;
    private EditText lance3;
    private TextView PointTour;
    private TextView PointRestantT;
    private TextView LegT;
    private TextView SetT;
    private Button boutonValide;
    private Button boutonReset;

    // Vieilles Declaration variables action partie
    private TextView PointRestantTemporaire;
    private TextView PointRestantRV;
    private TextView LegCompteur;
    private TextView SetCompteur;

    // Declaration variables Recycler view
    private ArrayList<RowItemPartie> mPartieList;
    private RecyclerView recyclerViewPartie;
    private MyAdapterPartie mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Variables partie
    private int ChoixScore;
    private int ChoixLeg;
    private int ChoixSet;
    private ArrayList<Joueurs> listeJoueurs;
    private ArrayList<String> listePseudos = new ArrayList<>();
    private ArrayList<Integer> listeScores = new ArrayList<>();
    private ArrayList<Integer> listeLegs = new ArrayList<>();
    private ArrayList<Integer> listeSets = new ArrayList<>();
    private ArrayList<Integer> listeRounds = new ArrayList<>();
    private ArrayList<Boolean> listeBooleanPEC = new ArrayList<>();
    private String IdPartie;
    int positionPartie = 0;
    ArrayList<Parties> listeParties = new ArrayList<>();
    private TextView TV_ScoreBoard;
    private Boolean ok = true;

    // Variables statistiques
    private String IdJoueur;
    private int PositionJoueur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // variables
        TV_ScoreBoard = findViewById(R.id.topScoreBoard);
        positionPartie = getIntent().getIntExtra("position", 0);     // recupere la valeur de la position de la partie dans firebase
        RecupJoueursPartie(positionPartie);

        // Variables TV
        lance1 = findViewById(R.id.edittext_lance_1);
        lance2 = findViewById(R.id.edittext_lance_2);
        lance3 = findViewById(R.id.edittext_lance_3);
        PointTour = findViewById(R.id.resultat);
        PointRestantT= findViewById(R.id.PointRestantTemporaire);
        LegT = findViewById(R.id.LegCompteur);
        SetT = findViewById(R.id.SetCompteur);

        boutonValide = findViewById(R.id.boutonValide);
        boutonReset = findViewById(R.id.boutonReset);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            // Valeur des scores avant d'avoir rentr?? les scores
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int temp1 = 0;
                int temp2 = 0;
                int temp3 = 0;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Si les 3 lanc??s sont rentr??s
                if (!lance1.getText().toString().equals("") && !lance2.getText().toString().equals("") && !lance3.getText().toString().equals("")) {
                    int temp1 = Integer.parseInt(lance1.getText().toString());
                    int temp2 = Integer.parseInt(lance2.getText().toString());
                    int temp3 = Integer.parseInt(lance3.getText().toString());

                    //Bash si PointTour > 180
                    if(temp1 + temp2 + temp3 > 180) {PointTour.setText(String.valueOf(0)); Toast.makeText(Partie.this, "Score au dessus de 180 ? Je pense que tu triches", Toast.LENGTH_SHORT).show();}

                    // PointTour prend la valeur de lanc??1 + lanc??2 + lanc??3
                    else {PointTour.setText(String.valueOf(temp1 + temp2 + temp3));}
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }

        };
        lance1.addTextChangedListener(textWatcher);
        lance2.addTextChangedListener(textWatcher);
        lance3.addTextChangedListener(textWatcher);

        // Clic bouton reset
        boutonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        // Clic bouton valider
        boutonValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecupIdJoueur();

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
            db.collection("Parties").document(IdPartie).update("scores", listeScores); // Mise ?? jour des donnees (score) dans la BDD
        }

        // Recup des legs gagnes des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeLegs = listeParties.get(positionPartie).getLegs();
        if (listeLegs.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationScore(listeLegs,listeJoueurs,0);
            db.collection("Parties").document(IdPartie).update("legs", listeLegs); // Mise ?? jour des donnees (legs) dans la BDD
        }

        // Recup des legs gagnes des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeSets = listeParties.get(positionPartie).getSets();
        if (listeSets.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationScore(listeSets,listeJoueurs,0);
            db.collection("Parties").document(IdPartie).update("sets", listeSets); // Mise ?? jour des donnees (sets) dans la BDD
        }

        // Recup des rounds des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeRounds = listeParties.get(positionPartie).getRounds();
        if (listeRounds.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationScore(listeRounds,listeJoueurs,0);
            db.collection("Parties").document(IdPartie).update("rounds", listeRounds); // Mise ?? jour des donnees (sets) dans la BDD
        }

        // Recup du boolean des joueurs, si liste vide (cas creation de partie), initialisation de la liste
        listeBooleanPEC = listeParties.get(positionPartie).getBooleanPartieEnCours();
        if (listeBooleanPEC.isEmpty()){ // Si liste vide : cas de creation de partie, affectation de 0 (0 legs gagnes pr l'instant)
            InitialisationBoolean(listeBooleanPEC,listeJoueurs,true);
            db.collection("Parties").document(IdPartie).update("booleanPartieEnCours", listeBooleanPEC); // Mise ?? jour des donnees (sets) dans la BDD
        }

    }

    public void InitialisationScore(ArrayList<Integer> listeInteger, ArrayList<Joueurs> listeSize, int choixPts){
        // Ajout de choixPts aux listes integer (score, leg, set)
        for (int i=0; i<listeSize.size(); i++){
            listeInteger.add(choixPts);
        }
    }

    public void InitialisationBoolean(ArrayList<Boolean> listeBoolean, ArrayList<Joueurs> listeSize, boolean bool){
        for (int i=0; i<listeSize.size(); i++){
            listeBoolean.add(bool);
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



    public void CalculOnClick() {

        // Recup des valeurs firebase pr affichage textView
        PointRestantT.setText(String.valueOf(listeScores.get(PositionJoueur)));
        LegT.setText(String.valueOf(listeLegs.get(PositionJoueur)));
        SetT.setText(String.valueOf(listeSets.get(PositionJoueur)));

        if (listeBooleanPEC.get(PositionJoueur)){ // si partieEnCours = true

            if (!lance1.getText().toString().equals("") && !lance2.getText().toString().equals("") && !lance3.getText().toString().equals("")) {
                int pr = Integer.parseInt(PointRestantT.getText().toString());
                int pt = Integer.parseInt(PointTour.getText().toString());

                // Si PointTour est sup??rieur ?? PointRestant alors PointTour vaut 0
                if (pr < pt) {
                    PointTour.setText(String.valueOf(0));
                    Toast.makeText(Partie.this, "Je crois que t'as fait trop l??", Toast.LENGTH_SHORT).show();
                }

                // Sinon PointRestantTemporaire = PointRestantTemporaire - PointTour
                // Et lanc??1,2,3 = null
                else {
                    PointRestantT.setText(String.valueOf(pr - pt));
                    ModifScoreFirebase(pr-pt);
                    ModifRoundFirebase();

                    lance1.setText(null);
                    lance2.setText(null);
                    lance3.setText(null);
                    PointTour.setText(String.valueOf(0));


                    // Si PointRestant = 0 : LegT = LegT + 1
                    if (PointRestantT.getText().toString().equals("0")) {

                        // +1 au leg
                        int l = Integer.parseInt(LegT.getText().toString());
                        LegT.setText(String.valueOf(l + 1));
                        ModifLegFirebase(l+1);

                        // Reset du score
                        PointRestantT.setText(String.valueOf(ChoixScore));
                        ModifScoreFirebase(ChoixScore);

                        PointTour.setText("0");

                        // Si Leg = ChoixLeg : SetT = SetT + 1
                        if (LegT.getText().toString() == String.valueOf(ChoixLeg)) {

                            // +1 au set
                            int s = Integer.parseInt(SetT.getText().toString());
                            SetT.setText(String.valueOf(s + 1));
                            ModifSetFirebase(s+1);

                            // Reset du leg
                            LegT.setText("0");
                            ModifLegFirebase(0);

                            if (SetT.getText().toString() == String.valueOf(ChoixSet)) {
                                Toast.makeText(Partie.this, "ON A GAGNEEEEEEE", Toast.LENGTH_SHORT).show();

                                // Incrementation des statistiques
                                IncrementationStatistiques();

                                ok = false;
                                ModifBooleanFirebase(false);

                                FinPartie();

                            }
                        }
                    }
                }
            if (ok){
                RefreshPartie();
            }
            } else {
                Toast.makeText(Partie.this, "Il manque au moins un lanc??", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Partie.this, "La partie est termin??e", Toast.LENGTH_SHORT).show();
            FinPartie();
        }
    }

    public void FinPartie(){

        // Pop up de confirmation
        AlertDialog.Builder alert = new AlertDialog.Builder(Partie.this);
        alert.setTitle("Fin de partie");
        ArrayList<Integer> listeRound = listeParties.get(positionPartie).getRounds();
        alert.setMessage("Partie terminee en " + listeRound.get(PositionJoueur) + " rounds !");

        alert.setPositiveButton("Genial !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Partie.this,"Partie terminee",Toast.LENGTH_SHORT).show();

            }
        });

        alert.setNegativeButton("Revenir au menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Partie.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
        alert.create().show();
    }

    public void RecupIdJoueur(){

        db.collection("Joueurs")
                .orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture RecupIdJoueurs", "Entre dans le oncomplete : ");
                        FirebaseUser currentUser = mAuth.getCurrentUser(); // recup du current user

                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Joueurs> downloadInfoList = task.getResult().toObjects(Joueurs.class); // Va chercher dans joueurs heritant users

                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    if (currentUser.getEmail().equals(downloadInfoList.get(i).getEmail())){ // recup que l'utilisateur
                                        IdJoueur = downloadInfoList.get(i).getEmail();
                                        Log.d("Waouh", "IdJoueur :" + IdJoueur);
                                    }

                                }

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }

                            for (int j=0; j<listeJoueurs.size();j++){
                                if (listeJoueurs.get(j).getEmail().equals(IdJoueur)){
                                    PositionJoueur = j;
                                    Log.d("Waouh", "PositionJoueur :" + PositionJoueur);
                                }
                            }

                            ModifFirebase();

                        }
                    }
                });
    }

    public void IncrementationStatistiques(){
        // statistiques
        db.collection("Joueurs").document(IdJoueur).update("nbSetGagnes", FieldValue.increment(ChoixLeg*ChoixSet));   // Increment de nb sets gegnes
        db.collection("Joueurs").document(IdJoueur).update("nbLegGagnes", FieldValue.increment(ChoixLeg));   // Increment de nb legs gegnes
        db.collection("Joueurs").document(IdJoueur).update("nbParties", FieldValue.increment(1));   // Increment de nb legs gegnes

    }

    public void ModifFirebase(){

        db.collection("Parties")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        listeParties.clear();

                        Log.d("Lecture Modif firebase", "Entre dans le oncomplete : ");
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Parties> downloadInfoList = task.getResult().toObjects(Parties.class); // Va chercher dans joueurs heritant users

                                for (int i = 0; i < downloadInfoList.size(); i++) {
                                    listeParties.add(downloadInfoList.get(i));
                                }

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }

                            CalculOnClick();

                        }
                    }
                });
    }

    public void ModifRoundFirebase(){
        ArrayList<Integer> listeNvxRound = new ArrayList<>();
        listeNvxRound = listeParties.get(positionPartie).getRounds();
        listeNvxRound.set(PositionJoueur,listeNvxRound.get(PositionJoueur)+1);
        db.collection("Parties").document(IdPartie).update("rounds", listeNvxRound);   // Ajout du nouveau round
    }

    public void ModifScoreFirebase(int nvxScore){
        ArrayList<Integer> listeNvxScores = new ArrayList<>();
        listeNvxScores = listeParties.get(positionPartie).getScores();
        listeNvxScores.set(PositionJoueur,nvxScore);
        db.collection("Parties").document(IdPartie).update("scores", listeNvxScores);   // Ajout du nouveau score
    }

    public void ModifSetFirebase(int nvxSet){
        ArrayList<Integer> listeNvxSets = new ArrayList<>();
        listeNvxSets = listeParties.get(positionPartie).getSets();
        listeNvxSets.set(PositionJoueur,nvxSet);
        db.collection("Parties").document(IdPartie).update("sets", listeNvxSets);   // Ajout du nouveau score
    }

    public void ModifLegFirebase(int nvxLeg){
        ArrayList<Integer> listeNvxLegs = new ArrayList<>();
        listeNvxLegs = listeParties.get(positionPartie).getLegs();
        listeNvxLegs.set(PositionJoueur,nvxLeg);
        db.collection("Parties").document(IdPartie).update("legs", listeNvxLegs);   // Ajout du nouveau score
    }

    public void ModifBooleanFirebase(boolean etatPartie){
        ArrayList<Boolean> listeNvxBool = new ArrayList<>();
        listeNvxBool = listeParties.get(positionPartie).getBooleanPartieEnCours();
        listeNvxBool.set(PositionJoueur,etatPartie);
        db.collection("Parties").document(IdPartie).update("booleanPartieEnCours", listeNvxBool);   // Ajout du nouveau score
    }

    public void RefreshPartie(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void Reset(){

    }

}