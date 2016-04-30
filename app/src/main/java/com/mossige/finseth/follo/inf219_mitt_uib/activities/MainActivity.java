package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CalendarFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.SettingFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.ConversationFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message.ChooseRecipientFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, CalendarFragment.OnDateClickListener{

    private static final String TAG = "MainActivity";

    private User profile;
    private ArrayList<Course> courses;
    private NavigationView navigationView;
    private int unreadCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set main layout
        setContentView(R.layout.activity_main);

        requestProfile();
        requestUnreadCount();
        courses = new ArrayList<>();
        // No course filter, need all events in calendar
        requestCourses(false);

        initCourseListFragment();

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void requestCourses(final boolean filterInstituteCourses) {
        final JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    courses.clear();
                    courses.addAll(JSONParser.parseAllCourses(response, filterInstituteCourses));

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException " + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
            }

        });

        RequestQueueHandler.getInstance(this).addToRequestQueue(coursesReq);
    }

    private void initCourseListFragment() {
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
            initCourseListFragment();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if(id == R.id.nav_calendar){
            initCalendarFragment();

            drawer.closeDrawer(navigationView);
            return true;
        }

        if (id == R.id.nav_about) {
//            AboutFragment aboutFragment = new AboutFragment();
            ChooseRecipientFragment crf = new ChooseRecipientFragment();
            transaction.replace(R.id.content_frame, crf);
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

    @Override
    public void setAgendas(ArrayList<CalendarEvent> events) {
        AgendaFragment agendaFragment = (AgendaFragment) getSupportFragmentManager().findFragmentById(R.id.agenda_container);
        if (agendaFragment != null) {
            agendaFragment.setAgendas(events);
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

    public void requestUnreadCount() {

        final JsonObjectRequest profileReq = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.getUnreadCountURL(), (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    unreadCount = JSONParser.parseUnreadCount(response);

                    setMenuCounter(R.id.nav_inbox, unreadCount);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        profileReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(this).addToRequestQueue(profileReq);
    }

    private void initCalendarFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CalendarFragment calendarFragment = new CalendarFragment();

        // Bundle course ids
        Bundle bundle = new Bundle();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Course c : courses) {
            ids.add(c.getId());
        }
        bundle.putIntegerArrayList("ids", ids);
        calendarFragment.setArguments(bundle);

        transaction.replace(R.id.content_frame, calendarFragment);
        transaction.commit();
    }

    public void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    private void showToast() {
        Toast.makeText(this, R.string.error_profile, Toast.LENGTH_SHORT).show();
    }
}
