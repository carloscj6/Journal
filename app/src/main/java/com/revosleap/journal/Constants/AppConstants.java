package com.revosleap.journal.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppConstants {




    public static class FirebaseConstants{


        static FirebaseAuth auth= FirebaseAuth.getInstance();
        static DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Users");
        private String userDisplay= auth.getCurrentUser().getDisplayName();
        private static String usrKey= auth.getCurrentUser().getUid();
        public static DatabaseReference userDb=dbref.child(usrKey);


    }
}
