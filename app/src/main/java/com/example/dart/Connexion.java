package com.example.dart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Connexion extends AppCompatActivity {

    private static final String TAG = "Connexion"; // Pour des tags d'erreurs / verifs

    EditText EdT_email, EdT_password;
    Button btn_newAccount, btn_forgotPwd, btn_connexion;
    ImageButton Imbtn_back;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Affectation des EditText
        EdT_email = (EditText) findViewById(R.id.id_email_zone);
        EdT_password = (EditText) findViewById(R.id.id_pwd_zone);
        // Affectation des boutons
        btn_newAccount = (Button) findViewById(R.id.id_btn_newAccount);
        btn_forgotPwd = (Button) findViewById(R.id.id_btn_forgotPwd);
        btn_connexion = (Button) findViewById(R.id.id_btn_connexion);
        // Affectation ImageButton
        Imbtn_back = (ImageButton) findViewById(R.id.id_backButton);

        // Bouton connexion : ouverture du menu
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    signIn(EdT_email.getText().toString(), EdT_password.getText().toString());// ligne à commenter pr se passer de l'authentification (pour tests)
                    //Intent launchActivity = new Intent(Connexion.this, Menu.class);
                    //startActivity(launchActivity);
                }
                catch (Exception e){
                    reload();
                }
            }
        });

        // Bouton pas encore de compte : Ouverture page Creation compte
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Connexion.this, CreationCompte.class);
                startActivity(launchActivity);
                finish();
            }
        });

        // Bouton mdp oublie : Ouverture page Mot de passe oublie
        btn_forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(Connexion.this, ForgotPwd.class);
                startActivity(launchActivity);
                finish();
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

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(Connexion.this,"Connexion en tant que \n\t" + currentUser.getEmail(),Toast.LENGTH_SHORT).show();
            updateUI(currentUser);
        }
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else {
                                Toast.makeText(Connexion.this, "Vérifier d'abord votre email puis réessayer", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Connexion.this,"Connexion échouée", Toast.LENGTH_SHORT).show();
                            // reload();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void reload() {
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Connexion.this,
                                    "Email de verification envoyé à \n\t" + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(Connexion.this,
                                    "Erreur dans l'envoi de l'email de verification.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END send_email_verification]
    }

    private void updateUI(FirebaseUser user) {
        Toast.makeText(Connexion.this,"Connexion en tant que \n\t" + user.getEmail(),Toast.LENGTH_SHORT).show();
        Intent launchActivity = new Intent(Connexion.this, Menu.class);
        startActivity(launchActivity);
        // finish();
    }
}
