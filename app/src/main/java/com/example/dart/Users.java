package com.example.dart;

/* Cette classe permet d'enregistrer les infos des
   différents utilisateurs dans Firestore Database. */

public class Users {

    // variables for storing our data.
    private String username, email, id;

    public Users() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public Users(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    // getter methods for all variables.
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    // setter method for all variables.
    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdt() {
        return id;
    }

    public void setIdt(String id) {
        this.id = id;
    }
}
