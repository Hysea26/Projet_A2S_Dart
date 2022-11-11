package com.example.dart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class accueil extends AppCompatActivity {
    private Button move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        move=findViewById(R.id.Move);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(accueil.this,Historique.class);
                startActivity(intent);
            }
        });
        }
    }
