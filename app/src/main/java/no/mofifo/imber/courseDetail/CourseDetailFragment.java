package no.mofifo.imber.courseDetail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import no.mofifo.imber.ImberApplication;
import no.mofifo.imber.R;
import no.mofifo.imber.fragments.AnnouncementFragment;
import no.mofifo.imber.fragments.FileBrowserFragment;
import no.mofifo.imber.listeners.ItemClickSupport;
import no.mofifo.imber.listeners.MainActivityListener;
import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.CalendarEvent;
import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.MittUibClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDetailFragment extends Fragment implements CourseDetailView {

    private static final String TAG = "CourseFragment";

    private RecyclerView mainList;

    private ArrayList<Announcement> announcements;
    private ArrayList<CalendarEvent> agendas;

    private Course course;

    /* If data is loaded */
    private boolean[] loaded;

    MainActivityListener mCallback;
    private MittUibClient mittUibClient;

    @BindString(R.string.error_announcements_list)
    String coursesErrorMessage;

    @BindString(R.string.snackbar_retry_text)
    String retryButtonText;

    @BindView(R.id.progressBar)
    SmoothProgressBar progressBar;

    /* This fragments presenter */
    @Inject
    CourseDetailPresenter presenter;

    /* Adapter binding content to the recycler view */
    @Inject
    CourseDetailAdapter adapter;

    public CourseDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerCourseDetailComponent.builder()
                .apiComponent(((ImberApplication) getActivity().getApplication()).getApiComponent())
                .courseDetailPresenterModule(new CourseDetailPresenterModule(this)).build()
                .inject(this);

        presenter.initialize(getArguments());

//        TODO:
//          - presenter.loadAnnouncements()
//          - presenter.loadEvents()

//        announcements = new ArrayList<>();
//        agendas = new ArrayList<>();
//        loaded = new boolean[3];
//
//
//        requestAnnouncements(course.getId());
//        requestAgendas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);
        presenter.onCreateView();
//        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    private void loadFileBrowserFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        FileBrowserFragment fileBrowserFragment = new FileBrowserFragment();
        transaction.replace(R.id.content_frame, fileBrowserFragment);
        transaction.addToBackStack(null);

        Bundle args = new Bundle();
        String json = new Gson().toJson(course);
        args.putString("course", json);
        fileBrowserFragment.setArguments(args);

        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainList.setVisibility(View.GONE);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        mainList.setAdapter(adapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                if (position == 0) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    AnnouncementFragment announcementFragment = new AnnouncementFragment();
                    transaction.replace(R.id.content_frame, announcementFragment);
                    transaction.addToBackStack(null);

                    Bundle args = new Bundle();
                    args.putString("courseCode", course.getCourseCode());
                    args.putInt("courseId", course.getId());
                    announcementFragment.setArguments(args);

                    transaction.addToBackStack(null);
                    transaction.commit();

                } else if (position == 1) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    mCallback.initCalendar();

                } else if (position == 2) {
                    // Clicked file browser card
                    loadFileBrowserFragment();

                }
            }
        });
    }

    private void requestAnnouncements(final int course_id) {

        Call<List<Announcement>> call = mittUibClient.getAnnouncements(course_id);
        call.enqueue(new Callback<List<Announcement>>() {
            @Override
            public void onResponse(Call<List<Announcement>> call, retrofit2.Response<List<Announcement>> response) {

                if (isAdded()) {
                    if (response.isSuccessful()) {
                        announcements.addAll(response.body());
                        loaded[0] = true;

                        if (isLoaded()) {
                            mainList.setVisibility(View.VISIBLE);
                            progressBar.progressiveStop();
                        }

                    } else {
                        progressBar.progressiveStop();
                        mCallback.showSnackbar(getString(R.string.error_announcements_list), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestAnnouncements(course_id);
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                if (isAdded()) {
                    progressBar.progressiveStop();
                    mCallback.showSnackbar(getString(R.string.error_announcements_list), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestAnnouncements(course_id);
                        }
                    });
                }
            }
        });
    }

    /**
     * @return Returns true if all data is loaded
     */
    private boolean isLoaded() {
        for (int i = 0; i < loaded.length; i++) {
            if (!loaded[i]) return false;
        }
        return true;
    }

    @Override
    public void displayAnnouncementsError() {
        Snackbar snackbar = Snackbar.make(getView(), coursesErrorMessage, Snackbar.LENGTH_LONG);
        snackbar.setAction(retryButtonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadAnnouncements();
            }
        });
    }

    private void requestAgendas() {

        //Course ids for context_codes in url
        ArrayList<String> contextCodes = new ArrayList<>();
        contextCodes.add("course_" + course.getId());

        //Type - event/assignment
        String type = "event";

        //Only 3 agendas for one course
        int perPage = 3;

        //Get todays date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String startDate = df.format(cal.getTime());
        String endDate = df.format(cal.getTime());

        Call<List<CalendarEvent>> call = mittUibClient.getEvents(startDate, endDate, contextCodes, null, type, perPage);
        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {

                if (isAdded()) {
                    if (response.isSuccessful()) {
                        agendas.clear();
                        agendas.addAll(response.body());

                        loaded[1] = true;

                        requestAssignments();

                        if (isLoaded()) {
                            mainList.setVisibility(View.VISIBLE);
                            progressBar.progressiveStop();
                        }

                    } else {
                        mCallback.showSnackbar(getString(R.string.error_requesting_agendas), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestAgendas();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_requesting_agendas), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestAgendas();
                        }
                    });
                }
            }
        });

    }

    private void requestAssignments() {
        //Course ids for context_codes in url
        ArrayList<String> contextCodes = new ArrayList<>();
        contextCodes.add("course_" + course.getId());

        //Type - event/assignment
        String type = "assignment";

        //Only 3 agendas for one course
        int perPage = 3;

        //Get todays date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String startDate = df.format(cal.getTime());
        String endDate = df.format(cal.getTime());

        Call<List<CalendarEvent>> call = mittUibClient.getEvents(startDate, endDate, contextCodes, null, type, perPage);
        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {

                if (isAdded()) {
                    if (response.isSuccessful()) {

                        agendas.addAll(response.body());

                        loaded[2] = true;

                        if (isLoaded()) {
                            mainList.setVisibility(View.VISIBLE);
                            progressBar.progressiveStop();
                        }
                    } else {
                        progressBar.progressiveStop();
                        mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestAssignments();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestAssignments();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.progressiveStop();
    }

    @Override
    public void displayAnnouncements(List<Announcement> announcements) {
    }
}
