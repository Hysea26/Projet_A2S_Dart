package com.example.dart;

import android.net.Uri;

import java.util.ArrayList;

public class Parties {


    // Variables stockage des donnees
    private ArrayList<Joueurs> joueursChecked;
    private int set, leg, score;
    private Uri imageURI;
    private boolean isSelected;

    public Parties() {
        // empty constructor
        // required for Firebase.
    }

    // Constructeur pour les variables
    public Parties(ArrayList<Joueurs> joueursChecked, int set, int leg, int score) {

        this.joueursChecked = joueursChecked;
        this.set = set;
        this.leg = leg;
        this.score = score;
    }

    // Get et Set llist joueurs

    public ArrayList<Joueurs> getJoueursChecked() {
        return joueursChecked;
    }

    public void setJoueursChecked(ArrayList<Joueurs> joueursChecked) {
        this.joueursChecked = joueursChecked;
    }



}
