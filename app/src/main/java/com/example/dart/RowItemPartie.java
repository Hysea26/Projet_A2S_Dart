package com.example.dart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RowItemPartie extends AppCompatActivity {
    private int mImgJoueur;
    private String mNom;
    private int mSet;
    private int mLeg;
    private int mPoint;
    private boolean mIsSelected;

    public RowItemPartie(int mImgJoueur, String mNom, int mSet, int mLeg, int mPoint, boolean mIsSelected) {
        mImgJoueur = mImgJoueur;
        mNom = mNom;
        mSet = mSet;
        mLeg = mLeg;
        mPoint = mPoint;
        mIsSelected = mIsSelected;

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

    public int getSet() {
        return mSet;
    }

    public int getLeg() {
        return mSet;
    }
    public int getPoint() {
        return mPoint;
    }
    public boolean getIsSelected() {
        return mIsSelected;
    }
}