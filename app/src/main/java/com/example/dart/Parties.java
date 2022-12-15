package com.example.dart;

import android.net.Uri;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parties {

    // Variables stockage des donnees
    private ArrayList<Joueurs> listeJoueursChecked;
    private ArrayList<Integer> listeSets,listeLegs,listeScores, listeRounds;
    private Integer choixSet,choixLeg, choixScore;
    private String idPartie;
    private ArrayList<Boolean> partieEnCours;

    public Parties() {
        // empty constructor
        // required for Firebase.
    }

    // Constructeur pour les variables
    public Parties(String idPartie, ArrayList<Joueurs> listeJoueursChecked, Integer choixSet, Integer choixLeg, Integer choixScore, ArrayList<Integer> listeSets, ArrayList<Integer> listeLegs, ArrayList<Integer> listeScores, ArrayList<Integer> listeRounds, ArrayList<Boolean> partieEnCours) {

        this.idPartie = idPartie;
        this.listeJoueursChecked = listeJoueursChecked;
        this.listeSets = listeSets;
        this.listeLegs = listeLegs;
        this.listeScores = listeScores;
        this.listeRounds = listeRounds;
        this.choixSet = choixSet;
        this.choixLeg = choixLeg;
        this.choixScore = choixScore;
        this.partieEnCours = partieEnCours;
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

    public ArrayList<Integer> getRounds() {
        return listeRounds;
    }

    public void setRounds(ArrayList<Integer> rounds) {
        this.listeRounds = rounds;
    }

    public Integer getChoixSet(){ return choixSet; }

    public void setChoixSet(Integer set){ this.choixSet = set; }

    public Integer getChoixLeg(){ return choixLeg; }

    public void setChoixLeg(Integer leg){ this.choixLeg = leg; }

    public Integer getChoixScore(){ return choixScore; }

    public void setChoixScore(Integer score){ this.choixScore = score; }

    public String getIdPartie(){ return idPartie; }

    public void setIdPartie(String id){ this.idPartie = id; }

    public ArrayList<Boolean> getBooleanPartieEnCours(){ return partieEnCours; }

    public void setBooleanPartieEnCours(ArrayList<Boolean> partieEnCours){ this.partieEnCours = partieEnCours; }

}
