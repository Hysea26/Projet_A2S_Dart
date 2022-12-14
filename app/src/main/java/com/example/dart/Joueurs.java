package com.example.dart;

import android.net.Uri;

public class Joueurs {

    // Variables stockage des donnees
    private String username, email;
    private int nbParties, meilleurLanceFlechette, nbLegGagnes, nbSetGagnes, nbAmis;
    private Uri imageURI;
    private boolean isSelected;

    public Joueurs() {
        // empty constructor
        // required for Firebase.
    }

    public Joueurs(String email){
        this.email = email;
    }

    // Constructeur pour les variables
    public Joueurs(String email, String username, int nbParties, int meilleurLanceFlechette, int nbLegGagnes, int nbSetGagnes, int nbAmis) {

        this.email = email;
        this.username = username;
        this.nbParties = nbParties;
        this.meilleurLanceFlechette = meilleurLanceFlechette;
        this.nbLegGagnes = nbLegGagnes;
        this.nbSetGagnes = nbSetGagnes;
        this.nbAmis = nbAmis;
    }

    // Get et Set Email

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Get et Set Pseudo

    public String getPseudo() {
        return username;
    }

    public void setPseudo(String username) {
        this.username = username;
    }

    // Get et Set Nombre de parties gagnees

    public int getNbParties() {
        return nbParties;
    }

    public void setNbParties(int title) {
        this.nbParties = title;
    }

    // Get et Set Meilleur lance de flechette

    public int getMeilleurLanceFlechette() {
        return meilleurLanceFlechette;
    }

    public void setMeilleurLanceFlechette(int meilleurLanceFlechette) {
        this.meilleurLanceFlechette = meilleurLanceFlechette;
    }

    // Get et Set Nombre de leg gagnes

    public int getnbLegGagnes() {
        return nbLegGagnes;
    }

    public void setnbLegGagnes(int nbLegGagnes) {
        this.nbLegGagnes = nbLegGagnes;
    }

    // Get et Set Nombre de set gagnes

    public int getnbSetGagnes() {
        return nbSetGagnes;
    }

    public void setnbSetGagnes(int nbSetGagnes) {
        this.nbSetGagnes = nbSetGagnes;
    }

    // Get et Set Nombre d'amis

    public int getnbAmis() {
        return nbAmis;
    }

    public void setnbAmis(int nbAmis) {
        this.nbAmis = nbAmis;
    }

}
