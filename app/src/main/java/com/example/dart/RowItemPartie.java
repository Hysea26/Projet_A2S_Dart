package com.example.dart;

public class RowItemPartie {

    private int mImgJoueur;
    private String mNom;
    private int mSet;
    private int mLeg;
    private int mPoint;

    public RowItemPartie(int ImgJoueur, String Nom, int Set, int Leg, int Point) {
        mImgJoueur = ImgJoueur;
        mNom = Nom;
        mSet = Set;
        mLeg = Leg;
        mPoint = Point;
    }

    public void changeTitre(String text) {
        mNom = text;
    }

    public int getImgUser() {
        return mImgJoueur;
    }

    public String getPseudo() {
        return mNom;
    }

    public Integer getSet() {
        return mSet;
    }

    public int getLeg() {
        return mLeg;
    }

    public int getPoint() {
        return mPoint;
    }

}