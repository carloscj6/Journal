package com.revosleap.journal.FirebaseUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.revosleap.journal.Constants.AppConstants;
import com.revosleap.journal.RecyclerUtils.ViewHolders.TimelineVholder;

public class EditEntry implements View.OnClickListener {
    Context context;
    DatabaseReference dbref=  new AppConstants.FirebaseConstants().userDb;



    public EditEntry(Context context) {
        this.context = context;
    }


    @Override
    public void onClick(View view) {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);

        String activity= preferences.getString("activity","");
        Toast.makeText(context, activity, Toast.LENGTH_SHORT).show();
    }
}
