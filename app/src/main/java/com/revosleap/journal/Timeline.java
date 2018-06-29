package com.revosleap.journal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DatabaseReference;
import com.revosleap.journal.Constants.AppConstants;
import com.revosleap.journal.Constants.AppStrings;
import com.revosleap.journal.Model.EntryModels;
import com.revosleap.journal.Model.FetchModel;
import com.revosleap.journal.RecyclerUtils.RecyclerView.RecyclerViewBasic;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Timeline extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextInputEditText entryText;
    private String text;
    private String activitykey;
    private String action;
    FloatingActionButton fab;
    private List<FetchModel> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        recyclerView= findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);
        entryText= findViewById(R.id.diary_input);
        fab = findViewById(R.id.fab);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);



        new RecyclerViewBasic(recyclerView,Timeline.this,models).loadrecycler(); //loading recyclerview
        new RecyclerViewBasic(recyclerView,Timeline.this,models).recyclerGesture();//recyclerviewGestures
            loadday();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntryText();
            }
        });

        text= new AppStrings(Timeline.this).activity();
        activitykey= new AppStrings(Timeline.this).activitykey();
        action= new AppStrings(Timeline.this).action();
        if (action.equals("edit")){
            entryText.setText(text);

        }
        else Log.v("logger", "nuhtin");
    }

    private void loadday(){
        String date= new AppStrings(Timeline.this).date();

        if (!new AppStrings(Timeline.this).today().equals(date)){
            entryText.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
        else entryText.setVisibility(View.VISIBLE);



    }



    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("activity","");
        editor.putString("activitykey","");
        editor.putString("action","");
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    private void setEntryText(){
        String today= new AppStrings(Timeline.this).today();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        DatabaseReference dbref= AppConstants.FirebaseConstants.userDb;
        DatabaseReference databaseReference=dbref.child(today);
        SharedPreferences.Editor editor= preferences.edit();
        if (action.equals("edit")){



            dbref.child(today).child(activitykey).child("journalEntry")
                    .setValue(entryText.getText().toString());
            entryText.setText("");
            editor.putString("activity","");
            editor.putString("activitykey","");
            editor.putString("action","");
            editor.apply();

        }
        else {
            String uInput= entryText.getText().toString();
            String time= new AppStrings(Timeline.this).time();
            String key= dbref.push().getKey();

            if (uInput.isEmpty()){
                entryText.setError("No entry");
            }
            else {
                EntryModels models= new EntryModels();
                models.setDate(Calendar.getInstance().getTime().toString());
                models.setTime(time);
                models.setKey(key);
                models.setJournalEntry(uInput);

                databaseReference.child(key).setValue(models);
                entryText.setText("");

                editor.putString("activity","");
                editor.putString("activitykey","");
                editor.putString("action","");
                editor.apply();
            }
        }
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
           Log.e("keyboard","exception while minimizing sof keyboard");
        }
    }




}
