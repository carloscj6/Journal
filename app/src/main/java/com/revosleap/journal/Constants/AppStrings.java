package com.revosleap.journal.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AppStrings {
    private Context context;

    public AppStrings(Context context) {
        this.context = context;
    }
    public String today(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("dateCal","");
    }
    public String activity(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("activity","");
    }
    public String activitykey(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("activitykey","");
    }
    public String action(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("action","");
    }
    public String time(){
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm");
        Calendar calendar= Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }
    public String date(){
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEE, MMM d");
        Calendar calendar= Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }
    public String profileP(){
        return Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getPhotoUrl()).toString();
    }
    public String nameUser(){
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
    }

}
