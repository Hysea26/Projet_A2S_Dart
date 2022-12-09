package com.example.dart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PartieItem  {
    private int mImgJoueur;
    private String mNom;
    private int mSet;
    private int mLeg;
    private int mPoint;

    public PartieItem(int mImgJoueur, String mNom, int mSet, int mLeg, int mPoint) {
        mImgJoueur = mImgJoueur;
        mNom = mNom;
        mSet = mSet;
        mLeg = mLeg;
        mPoint = mPoint;
    }

    public void changeTitre(String text) {
        mNom = text;
    }

    public int getmImgJoueur() {
        return mImgJoueur;
    }

    public String getmNom() {
        return mNom;
    }

    public int getmSet() {
        return mSet;
    }

    public int getmLeg() {
        return mSet;
    }
    public int getmPoint() {
        return mPoint;
    }
}