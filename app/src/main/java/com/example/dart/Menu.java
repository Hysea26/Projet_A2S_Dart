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
            }
        });

        // Initialisation :
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
        mExampleList.add(position, new RowItemJoueur(R.drawable.img_user_profil, "Ajout" + position, "This is Line 2"));
        mAdapter.notifyItemInserted(position);
    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeNbParties(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createExampleList() {

        mExampleList = new ArrayList<>();

        for(int i=0;i<strPseudoJoueurs.size();i++) {
            mExampleList.add(new RowItemJoueur(R.drawable.img_user_profil, strPseudoJoueurs.get(i).toString(), strNbPartiesJoueurs.get(i).toString()));
            Log.d("Waouh", "mExampleList :"+mExampleList);
        }
    }

    public void RecupJoueurs(){ // recup des joueurs deja crees
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

                Intent launchActivity = new Intent(Menu.this, Menu.class);
                launchActivity.putExtra("position", position);   // transmet la valeur de position à Afficher_article
                startActivity(launchActivity);
            }

            @Override
            public void onDeleteClick(int position) { // si item noir clique, supprime
                //removeItem(position);
            }
        });
    }


    public void removeItem(int position){

        // Pop up de confirmation
        AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
        alert.setTitle("Confirmation de suppression");
        alert.setMessage("Voulez vous supprimer l'article : " + strPseudoJoueurs.get(position) + " ? ");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Suppression dans l'affichage
                mExampleList.remove(position);
                mAdapter.notifyItemRemoved(position);

                // Suppression dans la base de donnees
                db.collection("Articles").document(strIdJoueurs.get(position))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                // Suppression dans les listes de valeurs des articles
                strPseudoJoueurs.remove(position);
                strNbPartiesJoueurs.remove(position);
                strIdJoueurs.remove(position);

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