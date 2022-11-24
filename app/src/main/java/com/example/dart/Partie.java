package com.example.dart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Partie extends AppCompatActivity {
    private Button backbtn;
    private EditText lance1;
    private EditText lance2;
    private EditText lance3;
    private TextView resultat;
    private Button buttonDouble;
    private Button buttonTriple;
    // Declaration variables Recycler view
    private ArrayList<PartieItem> mExampleList;
    private RecyclerView mrvPartie;
    //private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    ArrayList<String> strTtlArticles = new ArrayList<String>();
    ArrayList<String> strSumArticles = new ArrayList<String>();
    ArrayList<String> strPartieRobotArticles = new ArrayList<String>();
    ArrayList<String> strIdArticles = new ArrayList<String>();

    int images[] = {R.drawable.img_user_profil, R.drawable.img_user_profil};
    int JoueurSet[], JoueurLeg[], JoueurPoint[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);





        backbtn = (Button) findViewById(R.id.backbtn);
        lance1 = findViewById(R.id.edittext_lance_1);
        lance2 = findViewById(R.id.edittext_lance_2);
        lance3 = findViewById(R.id.edittext_lance_3);
        resultat = findViewById(R.id.resultat);
        buttonDouble = (Button) findViewById(R.id.buttonDouble);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNouvellePartie();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int temp1 = 0;
                int temp2 = 0;
                int temp3 = 0;


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!lance1.getText().toString().equals("") && !lance2.getText().toString().equals("") && !lance3.getText().toString().equals(""))
                {
                    int temp1 = Integer.parseInt(lance1.getText().toString());
                    int temp2 = Integer.parseInt(lance2.getText().toString());
                    int temp3 = Integer.parseInt(lance3.getText().toString());
                    resultat.setText(String.valueOf(temp1 + temp2 + temp3));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        lance1.addTextChangedListener(textWatcher);
        lance2.addTextChangedListener(textWatcher);
        lance3.addTextChangedListener(textWatcher);
    }

    public void openNouvellePartie() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);


    }
/*
    public void buildRecyclerView() {
        mrvPartie = findViewById(R.id.recyclerViewPartie);

        mAdapter = new MyAdapter(mExampleList); // cree avec mExampleList, a changer
        mrvArticles.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mrvArticles.setLayoutManager(mLayoutManager);

        mrvArticles.setHasFixedSize(true);

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { // si img couleur clique, change texte
                //changeItem(position, "Clicked");

                Intent launchActivity = new Intent(Menu.this, Afficher_article.class);
                launchActivity.putExtra("position", position);   // transmet la valeur de position à Afficher_article
                launchActivity.putExtra("liste", "non résolu");
                startActivity(launchActivity);
            }

            @Override
            public void onDeleteClick(int position) { // si item noir clique, supprime
                //removeItem(position);
            }
        });
    }
*/




}