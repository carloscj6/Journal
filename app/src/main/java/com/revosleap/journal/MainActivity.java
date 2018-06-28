package com.revosleap.journal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.revosleap.journal.Constants.AppStrings;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private CompactCalendarView calendarView;
        private FirebaseAuth auth= FirebaseAuth.getInstance();
        private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView= findViewById(R.id.compactcalendar_view);
        title= findViewById(R.id.date_title);
        if (auth.getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setCalendarView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,Timeline.class);
                startActivity(intent);
            }
        });
        todays();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navheader();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id== R.id.action_logout){
            auth.signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            // Handle the camera action
        }
        else if (id==R.id.nav_share){
            share();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setCalendarView(){
        calendarView.setFirstDayOfWeek(1);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor= preference.edit();
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEE, MMM d");
                String date=simpleDateFormat.format(dateClicked);

                editor.putString("dateCal",date);
                editor.apply();
                Intent intent= new Intent(MainActivity.this,Timeline.class);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
    }
    private void todays(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor= preferences.edit();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEE, MMM d");
        Calendar calendar= Calendar.getInstance();
        String date = simpleDateFormat.format(calendar.getTime());
        editor.putString("dateCal",date);
        editor.apply();
        title.setText(date);


    }
    private void navheader(){
        NavigationView navigationView= findViewById(R.id.nav_view);
        View view= navigationView.getHeaderView(0);
        CircleImageView imageView= view.findViewById(R.id.profilePic);
        TextView name= view.findViewById(R.id.nameTxt);
        Picasso.get().load(new AppStrings(MainActivity.this).profileP()).into(imageView);
        name.setText(new AppStrings(MainActivity.this).nameUser());
        //  Log.v("Purl",new AppStrings(Timeline))
        navigationView.setNavigationItemSelectedListener(this);

    }
    private void share(){
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Simple to use Journal");
        intent.putExtra(Intent.EXTRA_TEXT,new AppStrings(MainActivity.this).appUrl());
        intent.putExtra(Intent.EXTRA_TITLE,"Share App");
        startActivity(intent);
    }
}
