package com.revosleap.journal.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppStrings {
    private final Context context;

    public AppStrings(Context context) {
        this.context = context;
    }

    public String dateCal(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("dateCal","");

    }
}
