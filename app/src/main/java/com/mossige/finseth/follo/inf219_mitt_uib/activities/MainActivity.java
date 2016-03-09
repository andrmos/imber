package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AboutFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CalendarFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseListFragment;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, CalendarFragment.OnDateClickListener{

    private static final String TAG = "MainActivity";

    private User profile;
    private Bundle url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set main layout
        setContentView(R.layout.activity_main);

        requestProfile();

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

            CourseListFragment courseListFragment = new CourseListFragment();
            transaction.replace(R.id.content_frame, courseListFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_calendar){
            Log.i(TAG,"MyCal nav click");

            CalendarFragment calendarFragment = new CalendarFragment();
            calendarFragment.setArguments(url);
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

    private void requestProfile() {

        JsonObjectRequest profileReq = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.getUserProfileURL(), (String) null, new Response.Listener<JSONObject>() {

            String name;
            String email;
            String id;
            String login_id;
            String calendar;

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Got response");

                try {

                    id = response.getString("id");
                    name = response.getString("name");
                    email = response.getString("primary_email");
                    login_id = response.getString("login_id");
                    calendar = response.getJSONObject("calendar").get("ics").toString();

                    //Set name on navigation header
                    TextView nameTV = (TextView) findViewById(R.id.name);
                    nameTV.setText(name);

                    //Set email on navigation header
                    TextView emailTV = (TextView) findViewById(R.id.email);
                    emailTV.setText(email);

                    //Bundles calendarURL for later accessing
                    url = new Bundle();
                    url.putString("calendarURL",calendar);


                    //Creating user object
                    profile = new User(id,name,email,login_id,calendar);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());

            }
        });

        RequestQueueHandler.getInstance(this).addToRequestQueue(profileReq);
    }
}
