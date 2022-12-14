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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class Menu extends AppCompatActivity {

    private static final String TAG = "Menu";

    private Button NouvellePartieBtn;
    private ImageButton buttonInsert;

    private int position = 0;

    // Declaration variables Recycler view
    private ArrayList<RowItemJoueur> mExampleList;
    private RecyclerView mrvArticles;
    private MyAdapterJoueur mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> strPseudoJoueurs = new ArrayList<String>();
    ArrayList<Integer> strNbPartiesJoueurs = new ArrayList<Integer>();
    ArrayList<String> strIdJoueurs = new ArrayList<String>();

    // Declaration liste joueurs checkes par l'utilisateur a un instant t
    private ArrayList<Joueurs> mJoueursChecked = new ArrayList<Joueurs>();
    private Joueurs jCurrentUser;

    // Declaration des spinners
    private String choixSet, choixLeg, choixScore;

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

        // Variables Spinners
        Spinner spinnerChoixSet = findViewById(R.id.id_spin_Set);
        Spinner spinnerChoixLeg = findViewById(R.id.id_spin_Leg);
        Spinner spinnerChoixScore = findViewById(R.id.id_spin_Score);

        // Recycler view
        RecupJoueurs();
        //setButtons(); // clics du recycler

        // Bouton nouvelle partie
        NouvellePartieBtn = (Button) findViewById(R.id.NouvellePartieBtn);

        NouvellePartieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Menu.this, Partie.class);
                //startActivity(intent);
                choixSet = spinnerChoixSet.getSelectedItem().toString();
                choixLeg = spinnerChoixLeg.getSelectedItem().toString();
                choixScore = spinnerChoixScore.getSelectedItem().toString();
                RecupJoueursChecked();

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

    }

    //////////////////////////////// Fin OnCreate ///////////////////////////////////////

    public void insertItem(int position) {
        mExampleList.add(position, new RowItemJoueur(R.drawable.img_user_profil, "Ajout" + position, "This is Line 2",false));
        mAdapter.notifyItemInserted(position);
    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeNbParties(text);
        mAdapter.notifyItemChanged(position);
    }


    public void RecupJoueurs(){ // recup des joueurs deja crees pour affichage dans recycler view menu
        db.collection("Joueurs")
                .orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lecture RecupJoueurs", "Entre dans le oncomplete : ");
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        //Toast.makeText(Menu.this,"Connexion en tant que \n\t" + currentUser.getEmail(),Toast.LENGTH_SHORT).show();

                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Joueurs> downloadInfoList = task.getResult().toObjects(Joueurs.class); // Va chercher dans joueurs heritant users

                                for (int i=0; i<downloadInfoList.size(); i++) {

                                    if (!currentUser.getEmail().equals(downloadInfoList.get(i).getEmail())){ // pour ne pas afficher l'utilisateur
                                        // il faudra afficher uniquement les amis (boucle if)
                                        strPseudoJoueurs.add(downloadInfoList.get(i).getPseudo());
                                        strNbPartiesJoueurs.add(downloadInfoList.get(i).getNbParties());
                                        strIdJoueurs.add(downloadInfoList.get(i).getEmail());
                                    }


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

    public void createExampleList() {

        mExampleList = new ArrayList<>();

        for(int i=0;i<strPseudoJoueurs.size();i++) {
            mExampleList.add(new RowItemJoueur(R.drawable.img_user_profil, strPseudoJoueurs.get(i).toString(), strNbPartiesJoueurs.get(i).toString(),false));
            //Log.d("Create exemple list Waouh", "mExampleList :"+strPseudoJoueurs.get(i));
        }
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

                // pour lire si le checkbox du RowItem est check ou pas
                boolean isChecked = mExampleList.get(position).getIsSelected();
                Log.d("Waouh buildRecyclerView", "check :"+isChecked);
                mAdapter.notifyItemChanged(position);

            }

            @Override
            public void onDeleteClick(int position) { // si item noir clique, supprime
                //removeItem(position);
            }
        });
    }

    public void RecupJoueursChecked(){ // recup des joueurs checked

        db.collection("Joueurs")
                .orderBy("email", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Intent launchActivity = new Intent(Menu.this, Partie.class);
                        Log.d("Lecture RecupJoueursChecked", "Entre dans le oncomplete : ");

                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Joueurs> downloadInfoList = task.getResult().toObjects(Joueurs.class); // Va chercher dans joueurs heritant users

                                SuppressionCurrentUser(downloadInfoList); // Supprime le currentUser de la liste

                                for (int i=0; i<downloadInfoList.size(); i++) {
                                        // Regarde si le joueur est check
                                        boolean isChecked = mExampleList.get(i).getIsSelected();
                                        // Si le Joueur est check, recup du joueur dans la liste JoueursChecked pour lancement partie
                                        if (isChecked) {
                                            mJoueursChecked.add(downloadInfoList.get(i));
                                        }
                                }

                            } else {
                                Log.d("Echec", "Error getting documents: ", task.getException());
                            }
                        }

                        LancerPartie();
                        //Log.d("Waouh", "Joueurs checked :"+mJoueursChecked);
                    }
                });
    }

    // Fonction supprimant le currentUser d'une listej de joueurs
    public void SuppressionCurrentUser(List<Joueurs> listej){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        for (int i=0; i< listej.size();i++){
            if (listej.get(i).getEmail().equals(currentUser.getEmail())){
                jCurrentUser = listej.get(i);
                listej.remove(i);
            }
        }
    }

    public void InsertionCurrentUser(){

        // Copie de mJoueursChecked dans listeJ
        ArrayList<Joueurs> listeJ = new ArrayList<Joueurs>();
        for (int i=0; i<mJoueursChecked.size();i++){
            listeJ.add(mJoueursChecked.get(i));
        }

        // Vidage de mJoueursChecked
        mJoueursChecked.clear();

        // Ajout du joueur associe au currentUser dans mJoueursChecked
        mJoueursChecked.add(jCurrentUser);

        // Ajout des joueurs checkes dans mJoueursChecked
        for (int i=0; i<listeJ.size();i++){
            mJoueursChecked.add(listeJ.get(i));
        }

    }

    public void LancerPartie(){ // Recup des joueurs choisis pour la partie

        // Recup pseudos joueurs checked :
        ArrayList<String> p = new ArrayList<String>();
        for (int i=0; i<mJoueursChecked.size(); i++){
            p.add(mJoueursChecked.get(i).getPseudo());
        }
        Log.d("Waouh 3", "p :"+p);

        // Pop up de confirmation
        AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
        alert.setTitle("Lancement de partie");
        alert.setMessage("Voulez vous lancer la partie avec : " + p + " ? ");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Inserer le currentUser dans la liste mJoueursChecked
                InsertionCurrentUser();

                // Creer une partie dans Firestore
                creationDocumentPartieFirestore(mJoueursChecked,new ArrayList<Integer>(),new ArrayList<Integer>(),new ArrayList<Integer>(),new ArrayList<Integer>(),new ArrayList<Boolean>());

                // Recup de la position de la partie cree
                Intent intent = new Intent(Menu.this, Partie.class);
                db.collection("Parties")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null) {
                                        List<Parties> downloadInfoList = task.getResult().toObjects(Parties.class); // Va chercher dans joueurs heritant users
                                        position = downloadInfoList.size();

                                    } else {
                                        Log.d("Echec", "Error getting documents: ", task.getException());
                                    }
                                }
                                intent.putExtra("position", position-1); // envoi de la position de la partie a Partie
                                startActivity(intent);
                            }
                        });

                // Reset des variables utilisees pour la creation de la partie
                mJoueursChecked.clear();                        // vide la liste de joueurs checks
                for (int j=0; j<mExampleList.size();j++){       // met les cases en false (non cochees)
                    mExampleList.get(j).setIsSelected(false);
                }

            }
        });

        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(Menu.this,"Clic non",Toast.LENGTH_SHORT).show();

                mJoueursChecked.clear(); // vide la liste de joueurs checks
            }
        });
        alert.create().show();
    }

    private void creationDocumentPartieFirestore(ArrayList<Joueurs> listeJoueurs, ArrayList<Integer> listeSets, ArrayList<Integer> listeLegs,ArrayList<Integer> listeScores, ArrayList<Integer> listeRounds, ArrayList<Boolean> listeBooleanPEC) {

        // creating a collection reference
        // for our Firebase Firetore database.
        // CollectionReference dbCourses = db.collection("Users");

        String idPartie = CreationIdPartie();

        // adding our data to our users object class.
        Parties partie = new Parties(idPartie,listeJoueurs,Integer.parseInt(choixSet),Integer.parseInt(choixLeg),Integer.parseInt(choixScore),listeSets,listeLegs,listeScores,listeRounds,listeBooleanPEC);

        // Ajout de la data dans firestore
        db.collection("Parties")
                .document(idPartie)
                .set(partie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // after the data addition is successful we are displaying a success toast message.
                        Toast.makeText(Menu.this, "La partie " + idPartie + " a bien ete ajoute.", Toast.LENGTH_SHORT).show();
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

    private String CreationIdPartie (){

        // Recuperation de la date actuelle
        final Calendar date = Calendar.getInstance();
        int annee = date.get(date.YEAR);
        int mois = date.get(date.MONTH);
        int jour = date.get(date.DAY_OF_MONTH);
        String m = Convert(mois+1);
        String j = Convert(jour);
        String strDateActuelle = Integer.toString(annee)+m+j;

        // Recuperation de l'heure actuelle
        java.util.GregorianCalendar calendar = new GregorianCalendar();
        int heure = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);
        int seconde = calendar.get(Calendar.SECOND);
        String strHeureActuelle = Integer.toString(heure) + ":" + Integer.toString(minute) + ":" + seconde;

        String idPartie = strDateActuelle + "_" + strHeureActuelle; // Creation de l'IdArticle : date+heure

        return idPartie;
    }

    private String Convert (int jour){ // Fonction convert utile pour la creation de l'id partie
        String j;
        if (jour < 10){
            j = "0"+Integer.toString(jour);
        } else {
            j = Integer.toString(jour);
        }
        return j;
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