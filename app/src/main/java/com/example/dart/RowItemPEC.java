package com.example.dart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RowItemPEC extends AppCompatActivity {

    private int mImgUser;
    private String mPseudo;
    private String mLegende;

    public RowItemPEC(int imgUser, String pseudo, String legende) {
        mImgUser = imgUser;
        mPseudo = pseudo;
        mLegende = legende;
    }

    // Les gets et sets

    public void changeLegende(String text) {
        mLegende = text;
    }

    public int getImgUser() {
        return mImgUser;
    }

    public String getPseudo() {
        return mPseudo;
    }

    public String getLegende() {
        return mLegende;
    }


}