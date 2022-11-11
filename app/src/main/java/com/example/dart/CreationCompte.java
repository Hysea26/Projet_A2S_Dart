package com.example.dart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreationCompte extends AppCompatActivity {

    private static final String TAG = "Creation compte"; // Pour des tags d'erreurs / verifs

    EditText Edt_pseudo, EdT_email, EdT_password;
    Button btn_newAccount;
    ImageButton Imbtn_back;

    // creating a strings for storing our values from editText fields //
    private String username, email, idCompte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);

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
                    //addDataToFirestore(username, email ,idCompte); // a coder
                    //createAccount(email, EdT_password.getText().toString()); // a coder
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

    private void reload() {}

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




}