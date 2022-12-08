package com.example.dart;

import android.net.Uri;

import java.util.ArrayList;

public class Parties {


    // Variables stockage des donnees
    private ArrayList<Joueurs> listeJoueursChecked;
    private ArrayList<Integer> listeSets,listeLegs,listeScores;
    private Integer choixSet,choixLeg;
    private Uri imageURI;
    private boolean isSelected;

    public Parties() {
        // empty constructor
        // required for Firebase.
    }

    // Constructeur pour les variables
    public Parties(ArrayList<Joueurs> listeJoueursChecked, Integer choixSet, Integer choixLeg,ArrayList<Integer> listeSets, ArrayList<Integer> listeLegs, ArrayList<Integer> listeScores) {

        this.listeJoueursChecked = listeJoueursChecked;
        this.listeSets = listeSets;
        this.listeLegs = listeLegs;
        this.listeScores = listeScores;
        this.choixSet = choixSet;
        this.choixLeg = choixLeg;
    }

    // Get et Set liste joueurs

    public ArrayList<Joueurs> getJoueursChecked() {
        return listeJoueursChecked;
    }

    public void setJoueursChecked(ArrayList<Joueurs> joueurs) {
        this.listeJoueursChecked = joueurs;
    }

    public ArrayList<Integer> getSets() {
        return listeSets;
    }

    public void setSets(ArrayList<Integer> sets) {
        this.listeSets = sets;
    }

    public ArrayList<Integer> getLegs() {
        return listeLegs;
    }

    public void setLegs(ArrayList<Integer> legs) {
        this.listeLegs = legs;
    }

    public ArrayList<Integer> getScores() {
        return listeScores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.listeScores = scores;
    }

    public Integer getChoixSet(){ return choixSet; }

    public void setChoixSet(Integer set){ this.choixSet = set; }

    public Integer getChoixLeg(){ return choixLeg; }

    public void setChoixLeg(Integer leg){ this.choixLeg = leg; }

}
