package no.mofifo.imber.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import no.mofifo.imber.R;
import no.mofifo.imber.fragments.AboutFragment;
import no.mofifo.imber.fragments.CalendarFragment;
import no.mofifo.imber.fragments.SettingFragment;
import no.mofifo.imber.fragments.ConversationFragment;
import no.mofifo.imber.listeners.MainActivityListener;
import no.mofifo.imber.models.Course;
import no.mofifo.imber.models.Profile;
import no.mofifo.imber.retrofit.MittUibClient;

import no.mofifo.imber.fragments.CourseFragment;
import no.mofifo.imber.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, MainActivityListener {

    private static final String TAG = "MainActivity";

    private Profile profile;
    private ArrayList<Course> courses;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private MittUibClient mittUibClient;
    private double backButtonClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disableUriExposureCheck();

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        courses = new ArrayList<>();

        mittUibClient = ServiceGenerator.createService(MittUibClient.class, getApplicationContext());

        // TODO Update course ids after you have selected favorite courses
        requestCourses(); // Need events in calendar

        initCourseListFragment();

        // Setup toolbar and navigation drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer(toolbar);

        String profileJson = getIntent().getStringExtra("profile");
        if (profileJson != null) {
            profile = new Gson().fromJson(profileJson, Profile.class);
            updateNavDrawer();
        } else {
            // Only load profile if its not loaded in previous activity
            requestProfile();
        }
    }

    /**
     *  On SDK version 25 and up, we cannot expose file-URIs to other apps.
     *  This is done in the file browser when downloading files.
     *  This method disables some system checks, so we can expose file-URIs again.
     *  TODO: change to use FileProviders instead of URIs.
     */
    private void disableUriExposureCheck() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void initCourseListFragment() {
        CourseFragment fragment = new CourseFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, "courseList");
        transaction.addToBackStack("courseList");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (atCourseList()) {
                handleCourseListOnBack();

            } else {
                // Reload CourseListFragment if no other fragments exist
                int backstackCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backstackCount == 0) {
                    initCourseListFragment();
                } else {
                    super.onBackPressed();
                }
            }

        }
    }

    private void handleCourseListOnBack() {
        // Add some delay for second back press
        if (backButtonClickTime == 0) {
            backButtonClickTime = System.currentTimeMillis();
            confirmExitToast();

        } else {
            double elapsedTime = (System.currentTimeMillis() - backButtonClickTime) / 1000;
            if (elapsedTime <= 2.5) {
                // Quit app
                finish();

            } else {
                backButtonClickTime = 0;
                confirmExitToast();
            }
        }
    }

    private boolean atCourseList() {
        CourseFragment courseFragment = (CourseFragment) getSupportFragmentManager().findFragmentByTag("courseList");
        return courseFragment != null && courseFragment.isVisible();
    }

    private void confirmExitToast() {
        Toast.makeText(this, R.string.confirm_exit, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Setup fragment transaction for replacing fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Reset back stack when navigating to a new fragment from the nav bar
        getSupportFragmentManager().popBackStack("courseList", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        boolean fragmentTransaction = true;
        switch(id){

            case R.id.nav_course:
                getSupportFragmentManager().popBackStack();
                initCourseListFragment();
                break;

            case R.id.nav_calendar:
                initCalendarFragment();
                break;

            case R.id.nav_about:
                initFragment(new AboutFragment(),transaction);
                break;

            case R.id.nav_settings:
                initFragment(new SettingFragment(),transaction);
                break;

            case R.id.nav_inbox:
                initFragment(new ConversationFragment(), transaction);
                break;

            default:
                fragmentTransaction = false;
                break;
        }

        if(fragmentTransaction) {
            drawerLayout.closeDrawer(navigationView);
        }

        return fragmentTransaction;
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        // Setup navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setImberLogo();
    }

    private void setImberLogo() {
        TextView imberLogo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.imberLogo);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Pattaya-Regular.ttf");
        imberLogo.setTypeface(tf);
    }

    public void initCalendarFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CalendarFragment calendarFragment = new CalendarFragment();

        // Bundle course ids
        Bundle bundle = new Bundle();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Course c : courses) {
            ids.add(c.getId());
        }

        if (profile != null) {
            bundle.putInt("user_id", profile.getId());
        }

        bundle.putIntegerArrayList("ids", ids);
        calendarFragment.setArguments(bundle);

        transaction.replace(R.id.content_frame, calendarFragment);
        transaction.commit();
    }

    private void requestCourses() {
        Call<List<Course>> call = mittUibClient.getFavoriteCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {

                    courses.clear();
                    courses.addAll(response.body());

                } else {
                    showSnackbar();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                showSnackbar();
            }
        });
    }

    private void showSnackbar() {
        showSnackbar(getString(R.string.error_course_list), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCourses();
            }
        });
    }

    private void requestProfile() {
        Call<Profile> call = mittUibClient.getProfile();
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, retrofit2.Response<Profile> response) {
                if (response.isSuccessful()) {
                    profile = response.body();
                    updateNavDrawer();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                showSnackbar(getString(R.string.error_profile), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestProfile();
                    }
                });
            }
        });

    }

    private void updateNavDrawer() {
        //Set name on navigation header
        if (profile != null) {
            TextView nameTV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
            if (nameTV != null) {
                nameTV.setText(profile.getName());
            }

            //Set email on navigation header
            TextView emailTV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.primary_email);
            if (emailTV != null) {
                emailTV.setText(profile.getPrimary_email().toLowerCase());
            }
        }
    }

    @Override
    public void showSnackbar(String toastMessage, View.OnClickListener listener) {
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.content_frame), toastMessage, Snackbar.LENGTH_INDEFINITE);
        int duration = 4000;
        snackbar = snackbar.setDuration(duration);

        if(listener != null) {
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            snackbar.setAction(getString(R.string.snackback_action_text), listener);
        }

        if (!snackbar.isShown()) {
            snackbar.show();
        }

    }

    @Override
    public void initCalendar() {
        initCalendarFragment();
    }

    private void initFragment(Fragment fragment, FragmentTransaction transaction){
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}
