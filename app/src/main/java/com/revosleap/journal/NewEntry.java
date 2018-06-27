package com.revosleap.journal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.revosleap.journal.Constants.AppConstants;
import com.revosleap.journal.Model.EntryModels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewEntry extends AppCompatActivity {
    TextInputEditText newEntry;

    FloatingActionButton fab;
    String text,activitykey,action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        newEntry= findViewById(R.id.newEntry);
        fab =  findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(NewEntry.this);

        text= preferences.getString("activity","");
        activitykey= preferences.getString("activitykey","");
        action= preferences.getString("action","");
        if (action.equals("edit")){
            newEntry.setText(text);

        }
        else Log.v("logger", "nuhtin");




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }
    private void save(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(NewEntry.this);
        DatabaseReference dbref= AppConstants.FirebaseConstants.userDb;
        DatabaseReference databaseReference=dbref.child(preferences.getString("dateCal",""));
        if (action.equals("edit")){

            SharedPreferences.Editor editor= preferences.edit();

            dbref.child(preferences.getString("dateCal","")).child(activitykey).child("journalEntry")
                    .setValue(newEntry.getText().toString());
            newEntry.setText("");
            editor.putString("activity","");
            editor.putString("activitykey","");
            editor.putString("action","");
            editor.apply();

        }
        else {
        String uInput= newEntry.getText().toString();
        Calendar calendar= Calendar.getInstance();
        String date= Calendar.getInstance().getTime().toString();

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm");

        String time= simpleDateFormat.format(calendar.getTime());
        String key= dbref.push().getKey();



        if (uInput.isEmpty()){
            newEntry.setError("No entry");
        }
        else {
            EntryModels models= new EntryModels();
            models.setDate(date);
            models.setTime(time);
            models.setKey(key);
            models.setJournalEntry(uInput);

            databaseReference.child(key).setValue(models);
            newEntry.setText("");
        }
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(NewEntry.this);
        SharedPreferences.Editor editor= preferences.edit();

        newEntry.setText("");
        editor.putString("activity","");
        editor.putString("activitykey","");
        editor.putString("action","");
        editor.apply();
        super.onBackPressed();
    }
}

