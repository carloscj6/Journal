package com.revosleap.journal.FirebaseUtils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Persistence extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //allow offline accessibility of Firebase database
    }
}
