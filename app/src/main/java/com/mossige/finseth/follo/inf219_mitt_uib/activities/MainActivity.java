package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AboutFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CalendarFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.SettingFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.ConversationFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseListFragment;

import java.util.Date;

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

        CourseListFragment courseListFragment = new CourseListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, courseListFragment);
        transaction.commit();
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

        // Reset back stack when navigating to a new fragment from the nav bar
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if(id == R.id.nav_course){
            CourseListFragment courseListFragment = new CourseListFragment();
            transaction.replace(R.id.content_frame, courseListFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_calendar){
            CalendarFragment calendarFragment = new CalendarFragment();
            calendarFragment.setArguments(url);
            transaction.replace(R.id.content_frame, calendarFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if (id == R.id.nav_about) {
            AboutFragment aboutFragment = new AboutFragment();
            transaction.replace(R.id.content_frame, aboutFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_settings) {
            SettingFragment sf = new SettingFragment();
            transaction.replace(R.id.content_frame, sf);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_inbox) {
            ConversationFragment conversationFragment = new ConversationFragment();
            transaction.replace(R.id.content_frame, conversationFragment);
            transaction.commit();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_signin){
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
        }
    }

    private void requestProfile() {

        final JsonObjectRequest profileReq = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.getUserProfileURL(), (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    profile = JSONParser.parseUserProfile(response);

                    //Set name on navigation header
                    TextView nameTV = (TextView) findViewById(R.id.name);
                    nameTV.setText(profile.getName());

                    //Set email on navigation header
                    TextView emailTV = (TextView) findViewById(R.id.email);
                    emailTV.setText(profile.getEmail());

                    //Bundles calendarURL for later accessing
                    url = new Bundle();
                    url.putString("calendarURL", profile.getCalendar());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast();
            }
        });

        profileReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(this).addToRequestQueue(profileReq);
    }

    private void showToast() {
        Toast.makeText(this, R.string.error_profile, Toast.LENGTH_SHORT).show();
    }
}
