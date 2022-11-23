package com.example.dart;

public class RowItemJoueur {

    private int mImgUser;
    private String mPseudo;
    private String mNbParties;

    public RowItemJoueur(int imgUser, String pseudo, String nbParties) {
        mImgUser = imgUser;
        mPseudo = pseudo;
        mNbParties = nbParties;
    }

    public void changeNbParties(String text) {
        mNbParties = text;
    }

    public int getImgRobotPart() {
        return mImgUser;
    }

    public String getPseudo() {
        return mPseudo;
    }

    public String getNbParties() {
        return mNbParties;
    }
}