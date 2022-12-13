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
    private PartieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> strPseudoJoueurs = new ArrayList<String>();
    ArrayList<String> strIdJoueurs = new ArrayList<String>();
    ArrayList<Integer> point = new ArrayList<Integer>();

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Variables laetitia
    private int ChoixLeg;
    private int ChoixSet;
    private ArrayList<Integer> listeScores;
    int positionPartie = 0;
    ArrayList<Parties> listeParties = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // variables laetitia
        positionPartie = getIntent().getIntExtra("position", 0);     // recupere la valeur de la position de la partie dans firebase

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
                                    listeParties.add(downloadInfoList.get(position));
                                }

                                ChoixSet = listeParties.get(positionPartie).getChoixSet();
                                ChoixLeg = listeParties.get(positionPartie).getChoixLeg();

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }
                            createPartieList(); // Cree la liste de joueurs presents dans la BDD
                            buildRecyclerView(); // Construit le recycler view
                        }
                    }
                });
    }


    public void createPartieList() {


        mPartieList = new ArrayList<>();

        for (int i = 0; i < 2; i++) { //strPseudoJoueurs.size()
            mPartieList.add(new RowItemPartie(R.drawable.img_user_profil, "strPseudoJoueurs.get(i)",ChoixSet, ChoixLeg, 305));

        }

        Log.d("Waouh", "mPartieList :" + mPartieList);
    }

    public void buildRecyclerView() {
        recyclerViewPartie = findViewById(R.id.recyclerViewPartie);

        mAdapter = new PartieAdapter(mPartieList); // cree avec mPartieList
        recyclerViewPartie.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPartie.setLayoutManager(mLayoutManager);

        recyclerViewPartie.setHasFixedSize(true);


    }

    // fin recycler view interessant


}