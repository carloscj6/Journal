package com.revosleap.journal;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewActivity extends Fragment {
    FloatingActionButton fab;


    public NewActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new, container, false);
        fab= view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar= Snackbar.make(view,"Success",Snackbar.LENGTH_LONG);
                snackbar.show();
                Intent intent= new Intent(getContext(),Timeline.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
