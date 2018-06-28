package com.revosleap.journal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.revosleap.journal.Constants.AppConstants;
import com.revosleap.journal.FirebaseUtils.DiaryEntry;
import com.revosleap.journal.Model.EntryModels;
import com.revosleap.journal.Model.FetchModel;
import com.revosleap.journal.RecyclerUtils.Adapters.TlAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Timeline extends AppCompatActivity {
    RecyclerView recyclerView;
    List<FetchModel>modelsList;
    DatabaseReference dbref= new AppConstants.FirebaseConstants().userDb;
    Toolbar toolbar;
    TextInputEditText entryText;
    String text,activitykey,action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        recyclerView= findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);
        entryText= findViewById(R.id.diary_input);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

            loadTl();
            swipe();
            loadday();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEntryText();
            }
        });

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        text= preferences.getString("activity","");
        activitykey= preferences.getString("activitykey","");
        action= preferences.getString("action","");
        if (action.equals("edit")){
            entryText.setText(text);

        }
        else Log.v("logger", "nuhtin");
    }
    private void loadTl(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        toolbar.setTitle(preferences.getString("dateCal",""));
        dbref.child(preferences.getString("dateCal","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelsList= new ArrayList<>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    EntryModels model= dataSnapshot1.getValue(EntryModels.class);
                    FetchModel fetchModel= new FetchModel();
                    fetchModel.setDate(model.getDate());
                    fetchModel.setTime(model.getTime());
                    fetchModel.setJournalEntry(model.getJournalEntry());
                    fetchModel.setKey(model.getKey());
                    modelsList.add(fetchModel);

                    Collections.reverse(modelsList);
                    TlAdapter adapter= new TlAdapter(modelsList,Timeline.this);
                    RecyclerView.LayoutManager manager= new LinearLayoutManager(Timeline.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Timeline.this, "Db Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadday(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEE, MMM d");
        Calendar calendar= Calendar.getInstance();
        String date= simpleDateFormat.format(calendar.getTime());
        if (preferences.getString("dateCal","")!= date){
            entryText.setVisibility(View.GONE);
        }
        else entryText.setVisibility(View.VISIBLE);

    }
    private void swipe(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int id =(int) viewHolder.itemView.getTag();
//                dbref.removeValue();


            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    private void setEntryText(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        DatabaseReference dbref= AppConstants.FirebaseConstants.userDb;
        DatabaseReference databaseReference=dbref.child(preferences.getString("dateCal",""));
        SharedPreferences.Editor editor= preferences.edit();
        if (action.equals("edit")){



            dbref.child(preferences.getString("dateCal","")).child(activitykey).child("journalEntry")
                    .setValue(entryText.getText().toString());
            entryText.setText("");
            editor.putString("activity","");
            editor.putString("activitykey","");
            editor.putString("action","");
            editor.apply();

        }
        else {
            String uInput= entryText.getText().toString();
            Calendar calendar= Calendar.getInstance();
            String date= Calendar.getInstance().getTime().toString();

            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm");

            String time= simpleDateFormat.format(calendar.getTime());
            String key= dbref.push().getKey();



            if (uInput.isEmpty()){
                entryText.setError("No entry");
            }
            else {
                EntryModels models= new EntryModels();
                models.setDate(date);
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
    }


}
