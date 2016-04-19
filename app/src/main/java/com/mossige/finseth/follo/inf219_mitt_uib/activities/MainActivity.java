package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, CalendarFragment.OnDateClickListener{

    private static final String TAG = "MainActivity";

    private User profile;
    private Bundle url;

    private ArrayList<Course> courses;
    private ArrayList<CalendarEvent> events;

    private int page_num;

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

        //Check settings before intitializing courses
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Filter useless courses on institute level
        boolean filterInstituteCourses = sharedPreferences.getBoolean("checkbox_preference", true);

        courses = new ArrayList<>();
        events = new ArrayList<>();
        requestCourses(filterInstituteCourses);

        page_num = 1;

        //TODO Show spinner...?
    }

    private void requestCourses(final boolean filterInstituteCourses) {
        final JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    courses.clear();
                    courses.addAll(JSONParser.parseAllCourses(response, filterInstituteCourses));

                    initCourseListFragment();

                    requestCalendar();

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

        //Temporary lists for bundling course object
        Bundle bundle = new Bundle();
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> calendaer_urls = new ArrayList<>();
        ArrayList<String> course_codes = new ArrayList<>();

        for (Course c : courses) {
            ids.add(c.getId());
            names.add(c.getName());
            calendaer_urls.add(c.getCalenderUrl());
            course_codes.add(c.getCourseCode());
        }

        bundle.putIntegerArrayList("ids", ids);
        bundle.putStringArrayList("names", names);
        bundle.putStringArrayList("calendar_urls", calendaer_urls);
        bundle.putStringArrayList("course_codes", course_codes);

        courseListFragment.setArguments(bundle);

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
            initCalendarFragment(events);

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


    /**
     * Notify agenda fragment to update its calendar event cards to the specified date.
     * @param date The date clicked
     */
    @Override
    public void onDateSelected(Date date) {
        AgendaFragment agendaFragment = (AgendaFragment) getSupportFragmentManager().findFragmentById(R.id.agenda_container);
        if (agendaFragment != null) {
            agendaFragment.updateAgendaCards(date);

            Log.i(TAG, "onDateSelected: date:" + date);
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


                    //Get dates
//                    dt.execute(new URL(profile.getCalendar()));

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


    private void requestCalendar(){
        Log.i(TAG, "requestCalendar");

        //All course ids for context_codes in url
        ArrayList<String> ids = new ArrayList<>();
        for (Course c : courses) {
            ids.add("course_" + c.getId());
        }

        //What to exlude
        ArrayList<String> exclude = new ArrayList<>();
        exclude.add("child_events");
        String type = "event";

        //Todays date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        // Set the date to the 1st
        cal.set(Calendar.DATE, 1);
        String start_date = df.format(cal.getTime());

        // Add one month to the date
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        String end_date = df.format(cal.getTime());

        //Per page set to max
        String per_page = "50";

        JsonArrayRequest calendarEventsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCalendarEventsUrl(ids, exclude, type, start_date, end_date,per_page,page_num), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    //TODO clear?
                    ArrayList<CalendarEvent> tmpList = new ArrayList<>();

                    tmpList = JSONParser.parseAllCalendarEvents(response);
                    events.addAll(tmpList);

                    Log.i(TAG, "onResponse: events size" + events.size());
                    Log.i(TAG, "onResponse: last event" + events.get(events.size()-1));

                    if(tmpList.size() == 50) {
                        page_num++;
                        requestCalendar();
                    }


                } catch (JSONException e) {
                    Log.i(TAG, "exception: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
            }
        });

        RequestQueueHandler.getInstance(this).addToRequestQueue(calendarEventsRequest);
    }

    private void initCalendarFragment(ArrayList<CalendarEvent> events) {
        Log.i(TAG, "initCalendarFragment: size: " + events.size());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CalendarFragment calendarFragment = new CalendarFragment();

        Bundle bundle = new Bundle();

        //Temporary lists for bundling course object
        ArrayList<String> start_date = new ArrayList<>();
        ArrayList<String> end_date = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> location = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            start_date.add(df.format(events.get(i).getStartDate()));
            end_date.add(df.format(events.get(i).getEndDate()));
            name.add(events.get(i).getName());
            location.add(events.get(i).getLocation());
        }

        bundle.putStringArrayList("start_date", start_date);
        bundle.putStringArrayList("end_date", end_date);
        bundle.putStringArrayList("name", name);
        bundle.putStringArrayList("location", location);

        calendarFragment.setArguments(bundle);

        transaction.replace(R.id.content_frame, calendarFragment);
        transaction.commit();
    }

    private void showToast() {
        Toast.makeText(this, R.string.error_profile, Toast.LENGTH_SHORT).show();
    }
}
