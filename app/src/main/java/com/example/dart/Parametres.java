package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class Parametres extends AppCompatActivity {

    private static final String TAG = "Connexion"; // Pour des tags d'erreurs / verifs

    // Initialisation variables
    Button btn_deconnexion, btn_supprCompte;
    ImageButton Imbtn_stylo;
    TextView TV_pseudo_P, TV_email_P;
    private String idUser;

    // Initialisation variables firebase authentification et firestore
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        // Firebase authentification et firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Affectation des boutons
        Imbtn_stylo = (ImageButton) findViewById(R.id.id_img_stylo_P);
        btn_deconnexion = (Button) findViewById(R.id.id_btn_decoAccount_P);
        btn_supprCompte = (Button) findViewById(R.id.id_btn_delAccount_P);

        //Affectation des textview
        TV_pseudo_P = (TextView)findViewById(R.id.id_txt_pseudo_P);
        TV_email_P = (TextView)findViewById(R.id.id_txt_email_P);

        RecupUser();

        // Bouton modif profil (icone stylo) : changement icone
        Imbtn_stylo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Parametres.this, Parametres.class);
                startActivity(launchActivity);
            }
        });

        // Bouton deconnexion compte : deconnexion puis redirection vers page de connexion
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent launchActivity = new Intent(Parametres.this, Connexion.class);
                startActivity(launchActivity);
                finish();
            }
        });

        // Bouton suppression compte : suppression puis redirection vers MainActivity
        btn_supprCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteUser();   // Ne delete pas le doc dans Firestore
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d(TAG, "User account deleted.");
                            }
                        });
                // Supprimer aussi 'joueurs'
            }
        });


        // Initialisation bottom navigation :
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // On met Conversion en selectionne par defaut:
        bottomNavigationView.setSelectedItemId(R.id.nav_parametres);

        // Mise en place du Listenner:
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
//////////////////////////////////// fin onCreate ///////////////////////////////////

    public void RecupUser(){ // recup des informations de l'utilisateur pour affichage de son compte dans parametres
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Users> downloadInfoList = task.getResult().toObjects(Users.class);
                                for (int i=0; i<downloadInfoList.size(); i++) {
                                    if(user.getEmail().equals(downloadInfoList.get(i).getEmail())){
                                        TV_email_P.setText(downloadInfoList.get(i).getEmail());
                                        TV_pseudo_P.setText(downloadInfoList.get(i).getUsername());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void DeleteUser(){ // Fonction supprimant un utilisateur

        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Users> downloadInfoList = task.getResult().toObjects(Users.class);
                                for (int i=0; i<downloadInfoList.size(); i++) {
                                    if(user.getEmail().equals(downloadInfoList.get(i).getEmail())){
                                        idUser = downloadInfoList.get(i).getIdt();
                                    }
                                }
                            }
                        }
                    }
                });

        // Pop up de confirmation
        AlertDialog.Builder alert = new AlertDialog.Builder(Parametres.this);
        alert.setTitle("Confirmation de suppression");
        alert.setMessage("Voulez vous supprimer le compte d'adresse mail : " + user.getEmail()+" ? ");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Suppression dans la base de donnees
                db.collection("Users").document(idUser)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(Parametres.this,"Vous avez bien supprimé votre compte !",Toast.LENGTH_SHORT).show();
                                Intent launchActivity = new Intent(Parametres.this, MainActivity.class);
                                startActivity(launchActivity);
                                FirebaseAuth.getInstance().signOut();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

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
}