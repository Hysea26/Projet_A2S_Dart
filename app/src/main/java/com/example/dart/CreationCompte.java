package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class CreationCompte extends AppCompatActivity {

    private static final String TAG = "Creation compte"; // Pour des tags d'erreurs / verifs

    EditText Edt_pseudo, EdT_email, EdT_password;
    Button btn_newAccount;
    ImageButton Imbtn_back;

    // creating a strings for storing our values from editText fields //
    private String username, email, idCompte;

    // firebase authentification
    private FirebaseAuth mAuth;
    // creating a variable for firebasefirestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Getting our instance from Firebase Firestore //
        db = FirebaseFirestore.getInstance();

        // Affectation des EditText
        Edt_pseudo = (EditText) findViewById(R.id.id_pseudo_zone_CC);
        EdT_email = (EditText) findViewById(R.id.id_email_zone_CC);
        EdT_password = (EditText) findViewById(R.id.id_pwd_zone_CC);
        // Affectation des boutons
        btn_newAccount = (Button) findViewById(R.id.id_btn_CC);
        // Affectation ImageButton
        Imbtn_back = (ImageButton) findViewById(R.id.id_backButton_CC);


        // Bouton creation compte
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = Edt_pseudo.getText().toString();
                email = EdT_email.getText().toString();
                idCompte = CreationId(email,username);

                if (isEmailValid(email) && isPasswordValid(EdT_password.getText().toString()) && !username.isEmpty()) {
                    // calling method to add data to Firebase Firestore.
                    addDataToFirestore(username, email ,idCompte); // a coder
                    createAccount(email, EdT_password.getText().toString());
                    Intent launchActivity = new Intent(CreationCompte.this, Connexion.class);
                    startActivity(launchActivity);
                }
                else {
                    Toast.makeText(CreationCompte.this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Image Bouton retour : retour a la page precedente
        Imbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    /////////////////////////////////// Fin du OnCreate ////////////////////////////////////



    // Fonction mettant un "0" devant les jours 1 a 9 : 01,02,...10,11 pour que les id aient le meme nombre de caracteres
    private String Convert (int jour){
        String j;
        if (jour < 10){
            j = "0"+Integer.toString(jour);
        } else {
            j = Integer.toString(jour);
        }
        return j;
    }

    // Fonction creant un id different pour chaque utilisateur en utilisant son adresse mail, pseudo et heure de creation
    public String CreationId(String adressemail,String username){
        final Calendar date = Calendar.getInstance();
        int annee = date.get(date.YEAR);
        int mois = date.get(date.MONTH);
        int jour = date.get(date.DAY_OF_MONTH);
        String m = Convert(mois+1);
        String j = Convert(jour);
        String strDateActuelle = Integer.toString(annee) + m + j;

        // Recuperation de l'heure actuelle
        java.util.GregorianCalendar calendar = new GregorianCalendar();
        int heure = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);
        int seconde = calendar.get(Calendar.SECOND);
        String strHeureActuelle = Integer.toString(heure) + ":" + Integer.toString(minute) + ":" + seconde;

        idCompte = adressemail.substring(0,2) + username.substring(0,2) + strDateActuelle + "_" + strHeureActuelle;

        return idCompte;
    }

    // Fonction validant l'authenticite d'une adresse email
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // Fonction validant que le mot de passe soit possible (different de 0 et superieur a 5 caracteres)
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreationCompte.this,"Creation de compte reussie", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreationCompte.this,"Creation de compte echouee", Toast.LENGTH_SHORT).show();
                            reload();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreationCompte.this,
                                    "Email de verification envoye à\n\t " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(CreationCompte.this,
                                    " Erreur dans l'envoi de l'email de verification. ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END send_email_verification]
    }

    private void addDataToFirestore(String username, String email, String identifiant) {

        // creating a collection reference
        // for our Firebase Firetore database.
        //CollectionReference dbCourses = db.collection("Users");

        // adding our data to our users object class.
        Users users = new Users(username,email,identifiant);

        // Ajout de la data dans firestore
        db.collection("Users")
                .document(String.valueOf(idCompte))
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // after the data addition is successful we are displaying a success toast message.
                        Toast.makeText(CreationCompte.this, "L'utilisateur " + username + " a bien ete ajoute.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when the data addition process is failed. displaying a toast message when data addition is failed.
                        // Log.w(TAG, "Error writing document", e);
                        Toast.makeText(CreationCompte.this, "Erreur dans l'ajout de l'utilisateur \n" + e, Toast.LENGTH_SHORT).show();
                    }
                });


        // adding our data to our users object class.
        Joueurs joueur = new Joueurs(email,username,"","","","","");

        // Ajout de la data dans firestore
        db.collection("Joueurs")
                .document(String.valueOf(email))
                .set(joueur)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // after the data addition is successful we are displaying a success toast message.
                        Toast.makeText(CreationCompte.this, "Le joueur " + email + " a bien ete ajoute.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when the data addition process is failed. displaying a toast message when data addition is failed.
                        // Log.w(TAG, "Error writing document", e);
                        Toast.makeText(CreationCompte.this, "Erreur dans l'ajout du joueur \n" + e, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void reload() {}



}