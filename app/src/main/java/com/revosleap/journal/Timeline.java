package com.revosleap.journal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.revosleap.journal.Constants.AppConstants;
import com.revosleap.journal.Model.EntryModels;
import com.revosleap.journal.Model.FetchModel;
import com.revosleap.journal.RecyclerUtils.Adapters.TlAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Timeline extends AppCompatActivity {
    RecyclerView recyclerView;
    List<FetchModel>modelsList;
    DatabaseReference dbref= new AppConstants.FirebaseConstants().userDb;
    CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        recyclerView= findViewById(R.id.recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarLayout= findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

            loadTl();
            swipe();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Timeline.this,NewEntry.class);
                startActivity(intent);
            }
        });
    }
    private void loadTl(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(Timeline.this);
        toolbarLayout.setTitle(preferences.getString("dateCal",""));
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
}
