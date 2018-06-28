package com.revosleap.journal.RecyclerUtils.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.revosleap.journal.Model.FetchModel;
import com.revosleap.journal.R;

import java.util.ArrayList;
import java.util.List;

public class TimelineVholder extends RecyclerView.ViewHolder  {


    public TextView activity;
   public TextView time;
   private TimelineView timelineView;
    public TimelineVholder(View itemView, int viewType) {
        super(itemView);
        activity= itemView.findViewById(R.id.textActivity);
        time=itemView.findViewById(R.id.textTime);
        timelineView= itemView.findViewById(R.id.time_marker);
        timelineView.initLine(viewType);


    }



}
