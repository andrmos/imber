package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AboutFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CalendarFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseListFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, CalendarFragment.OnDateClickListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set main layout
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Setup fragment transaction for replacing fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        if(id == R.id.nav_course){
            Log.i(TAG,"Course nav click");

            CourseListFragment courseFragment = new CourseListFragment();
            transaction.replace(R.id.content_frame, courseFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_calendar){
            Log.i(TAG,"MyCal nav click");

            CalendarFragment calendarFragment = new CalendarFragment();
            transaction.replace(R.id.content_frame, calendarFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if (id == R.id.nav_about) {
            Log.i(TAG, "About nav click");

            AboutFragment aboutFragment = new AboutFragment();
            transaction.replace(R.id.content_frame, aboutFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }


        if(id == R.id.nav_signin){
            Log.i(TAG, "Sign in nav click");

            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }


    /**
     * Notify agenda fragment to update its calendar event cards to the specified date.
     * @param date The date clicked
     */
    @Override
    public void onDateSelected(Date date) {
        AgendaFragment agendaFragment = (AgendaFragment) getSupportFragmentManager().findFragmentById(R.id.agenda_container);

        if (agendaFragment != null) {

            agendaFragment.updateAgendaCards(date);

        } else {
            Log.i(TAG, "Error: agendaFragment is null");
        }
    }
}
