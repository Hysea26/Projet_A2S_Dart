package com.example.dart;

public class RowItemJoueur {

    private int mImgUser;
    private String mPseudo;
    private String mNbParties;
    private boolean mIsSelected;

    public RowItemJoueur(int imgUser, String pseudo, String nbParties, boolean isSelected) {
        mImgUser = imgUser;
        mPseudo = pseudo;
        mNbParties = nbParties;
        mIsSelected = isSelected;
    }

    // Les gets et sets

    public void changeNbParties(String text) {
        mNbParties = text;
    }

    public int getImgUser() {
        return mImgUser;
    }

    public String getPseudo() {
        return mPseudo;
    }

    public String getNbParties() {
        return mNbParties;
    }

    public boolean getIsSelected(){
        return mIsSelected;
    }

    public void setIsSelected(boolean isSelected){
        mIsSelected = isSelected;
    }

}