package com.revosleap.journal.RecyclerUtils.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

public class RecyclerViewBasic  {
    private RecyclerView recyclerView;
    private Context context;
    private DatabaseReference dbref= AppConstants.FirebaseConstants.userDb;
    private List<FetchModel> modelList;


    public RecyclerViewBasic(RecyclerView recyclerView, Context context, List<FetchModel> modelList) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.modelList= modelList;
    }
    public RecyclerViewBasic(Context context, List<FetchModel> models) {
        this.context = context;
        this.modelList = models;
    }


    public void loadrecycler(){

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        String date= preferences.getString("dateCal","");
        modelList= new ArrayList<>();
        dbref.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                   EntryModels model= dataSnapshot1.getValue(EntryModels.class);
                   FetchModel fetchModel= new FetchModel();
                   fetchModel.setDate(model.getDate());
                   fetchModel.setTime(model.getTime());
                   fetchModel.setJournalEntry(model.getJournalEntry());
                   fetchModel.setKey(model.getKey());
                   modelList.add(fetchModel);

                   Collections.reverse(modelList);
                   TlAdapter adapter= new TlAdapter(modelList,context);
                   RecyclerView.LayoutManager manager= new LinearLayoutManager(context);
                   recyclerView.setLayoutManager(manager);
                   recyclerView.setAdapter(adapter);

               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void recyclerGesture(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHoldr, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position= viewHolder.getAdapterPosition();

                TlAdapter adapter= new TlAdapter(context,modelList);
                //modelList= new ArrayList<>();


//                modelList= adapter.getkey();
//
//                adapter.notifyItemRemoved(position);
//                modelList.remove(position);

            }
        }).attachToRecyclerView(recyclerView);

    }


}
