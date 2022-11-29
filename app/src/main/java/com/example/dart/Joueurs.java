package com.example.dart;

import android.net.Uri;

public class Joueurs {


    // Variables stockage des donnees
    private String username, nbParties, meilleurLanceFlechette, nbLegGagnes, nbSetGagnes, nbAmis, email;
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
    public Joueurs(String email, String username, String nbParties, String meilleurLanceFlechette, String nbLegGagnes, String nbSetGagnes, String nbAmis) {
        if (nbParties.trim().equals("")) {
            nbParties = "0";
        }

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

    public String getNbParties() {
        return nbParties;
    }

    public void setNbParties(String title) {
        this.nbParties = title;
    }

    // Get et Set Meilleur lance de flechette

    public String getMeilleurLanceFlechette() {
        return meilleurLanceFlechette;
    }

    public void setMeilleurLanceFlechette(String meilleurLanceFlechette) {
        this.meilleurLanceFlechette = meilleurLanceFlechette;
    }

    // Get et Set Nombre de leg gagnes

    public String getnbLegGagnes() {
        return nbLegGagnes;
    }

    public void setnbLegGagnes(String nbLegGagnes) {
        this.nbLegGagnes = nbLegGagnes;
    }

    // Get et Set Nombre de set gagnes

    public String getnbSetGagnes() {
        return nbSetGagnes;
    }

    public void setnbSetGagnes(String nbSetGagnes) {
        this.nbSetGagnes = nbSetGagnes;
    }

    // Get et Set Nombre d'amis

    public String getnbAmis() {
        return nbAmis;
    }

    public void setnbAmis(String nbAmis) {
        this.nbAmis = nbAmis;
    }

}
