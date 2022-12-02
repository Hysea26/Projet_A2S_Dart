package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class Menu extends AppCompatActivity {

    private static final String TAG = "Menu";

    private Button NouvellePartieBtn;
    private ImageButton buttonInsert;

    // Declaration variables Recycler view
    private ArrayList<RowItemJoueur> mExampleList;
    private RecyclerView mrvArticles;
    private MyAdapterJoueur mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> strPseudoJoueurs = new ArrayList<String>();
    ArrayList<String> strNbPartiesJoueurs = new ArrayList<String>();
    ArrayList<String> strIdJoueurs = new ArrayList<String>();

    ArrayList<Joueurs> mJoueursChecked = new ArrayList<Joueurs>();

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Recycler view
        RecupJoueurs();
        //setButtons(); // clics du recycler

        NouvellePartieBtn = (Button) findViewById(R.id.NouvellePartieBtn);

        NouvellePartieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Partie.class);
                startActivity(intent);
                //RecupJoueurs2();
                //RecupJoueursCheked();

            }
        });

        // Initialisation Bottom navigation view :
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Par defaut, sur la page menu :
        bottomNavigationView.setSelectedItemId(R.id.nav_menu);

        // Mise en place du Listenner :
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

    } //////////////////////////////// Fin OnCreate ///////////////////////////////////////

    public void insertItem(int position) {
        mExampleList.add(position, new RowItemJoueur(R.drawable.img_user_profil, "Ajout" + position, "This is Line 2",false));
        mAdapter.notifyItemInserted(position);
    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeNbParties(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createExampleList() {

        mExampleList = new ArrayList<>();

        for(int i=0;i<strPseudoJoueurs.size();i++) {
            mExampleList.add(new RowItemJoueur(R.drawable.img_user_profil, strPseudoJoueurs.get(i).toString(), strNbPartiesJoueurs.get(i).toString(),false));
            //Log.d("Create exemple list Waouh", "mExampleList :"+strPseudoJoueurs.get(i));
        }
    }

    public void RecupJoueurs(){ // recup des joueurs deja crees pour affichage dans recycler view menu
        db.collection("Joueurs")
                .orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture", "Entre dans le oncomplete : ");
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Joueurs> downloadInfoList = task.getResult().toObjects(Joueurs.class); // Va chercher dans joueurs heritant users
                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    // il faudra afficher uniquement les amis (boucle if)
                                        strPseudoJoueurs.add(downloadInfoList.get(i).getPseudo());
                                        strNbPartiesJoueurs.add(downloadInfoList.get(i).getNbParties());
                                        strIdJoueurs.add(downloadInfoList.get(i).getEmail());

                                }
                                Log.d("Waouh", "Pseudos :"+strPseudoJoueurs);

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }
                            createExampleList(); // Cree la liste de joueurs presents dans la BDD
                            buildRecyclerView(); // Construit le recycler view
                        }
                    }
                });
    }

    public void RecupJoueurs2(){ // recup des joueurs checked

        db.collection("Joueurs")
                .orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture", "Entre dans le oncomplete : ");
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Joueurs> downloadInfoList = task.getResult().toObjects(Joueurs.class); // Va chercher dans joueurs heritant users

                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    // Regarde si le joueur est check
                                    boolean isChecked = mExampleList.get(i).getIsSelected();
                                    // Si le Joueur est check, recup du joueur dans la liste JoueursChecked pour lancement partie
                                    if (isChecked){
                                        mJoueursChecked.add(downloadInfoList.get(i));
                                    }

                                }
                                //Log.d("Waouh RJ2", "Pseudos :"+JoueursChecked);

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }
                            Log.d("Waouh RJ2", "Joueurs checked :"+mJoueursChecked);
                        }
                    }
                });
    }




    public void buildRecyclerView() {
        mrvArticles = findViewById(R.id.id_RV_joueurs);

        mAdapter = new MyAdapterJoueur(mExampleList); // cree avec mExampleList, a changer
        mrvArticles.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mrvArticles.setLayoutManager(mLayoutManager);

        mrvArticles.setHasFixedSize(true);

        mAdapter.setOnItemClickListener(new MyAdapterJoueur.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { // si img couleur clique, change texte
                //changeItem(position, "Clicked");

                //Intent launchActivity = new Intent(Menu.this, Menu.class);
                //launchActivity.putExtra("position", position);   // transmet la valeur de position à Afficher_article
                //startActivity(launchActivity);

                // pour lire si le checkbox du RowItem est check ou pas
                boolean isChecked = mExampleList.get(position).getIsSelected();
                Log.d("Waouh2", "check :"+isChecked);
                mAdapter.notifyItemChanged(position);

            }

            @Override
            public void onDeleteClick(int position) { // si item noir clique, supprime
                //removeItem(position);
            }
        });
    }

    // fin recycler view interessant


    public void RecupJoueursCheked(){ // Recup des joueurs choisis pour la partie

        // Recup pseudos joueurs checked :
        ArrayList<String> p = new ArrayList<String>();
        for (int i=0; i<mJoueursChecked.size(); i++){
            p.add(mJoueursChecked.get(i).getPseudo());
        }
        Log.d("Waouh3", "p :"+p);

        // Pop up de confirmation
        AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
        alert.setTitle("Lancement de partie");
        alert.setMessage("Voulez vous lancer la partie avec : " + mJoueursChecked + " ? ");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Aller dans partie
                Intent intent = new Intent(Menu.this, Partie.class);
                startActivity(intent);

            }
        });

        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(Menu.this,"Clic non",Toast.LENGTH_SHORT).show();

            }
        });
        alert.create().show();
    }



    private void addDataToFirestore(String username, String email, String identifiant) {

        // creating a collection reference
        // for our Firebase Firetore database.
        //CollectionReference dbCourses = db.collection("Users");

        // adding our data to our users object class.
        Parties partie = new Parties(mJoueursChecked,0,0,0);

        // Ajout de la data dans firestore
        db.collection("Parties")
                .document("Partie1")
                .set(partie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // after the data addition is successful we are displaying a success toast message.
                        Toast.makeText(Menu.this, "La partie 1" + email + " a bien ete ajoute.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when the data addition process is failed. displaying a toast message when data addition is failed.
                        // Log.w(TAG, "Error writing document", e);
                        Toast.makeText(Menu.this, "Erreur dans l'ajout de la partie \n" + e, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void setButtons() {
        buttonInsert = findViewById(R.id.searchbtn);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem(0);
            }
        });
    }




}