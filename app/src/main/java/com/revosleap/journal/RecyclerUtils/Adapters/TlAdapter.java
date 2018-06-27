package com.revosleap.journal.RecyclerUtils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.vipulasri.timelineview.TimelineView;
import com.revosleap.journal.FirebaseUtils.EditEntry;
import com.revosleap.journal.Model.FetchModel;
import com.revosleap.journal.NewEntry;
import com.revosleap.journal.R;
import com.revosleap.journal.RecyclerUtils.ViewHolders.TimelineVholder;

import java.util.List;

public class TlAdapter extends RecyclerView.Adapter<TimelineVholder> {
    List<FetchModel> models;
    Context context;


    public TlAdapter(List<FetchModel> models,Context context) {
        this.models = models;
        this.context= context;
    }





    @NonNull
    @Override
    public TimelineVholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.timeline,parent,false);
        TimelineVholder vholder= new TimelineVholder(view,viewType);





       return new TimelineVholder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineVholder holder, final int position) {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor=preferences.edit();

            FetchModel list= models.get(position);
            String date= list.getDate();
            String activity= list.getJournalEntry();
            String key=list.getKey();

            String time= list.getTime();

            holder.activity.setText(activity);
            holder.time.setText(time);

            holder.activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String txt= models.get(position).getJournalEntry();
                    String key= models.get(position).getKey();
                    editor.putString("activity",txt);
                    editor.putString("activitykey",key);
                    editor.putString("action","edit");
                    editor.apply();
                    Intent intent= new Intent(context, NewEntry.class);
                    context.startActivity(intent);
                }
            });



    }




    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }
    @Override
    public int getItemCount() {

        int arr=0;
        try {
            if (models.size()==0){
                arr=0;
            }
            else {
                arr=models.size();
            }
        }
        catch (Exception e){}
        return arr;
    }
}
