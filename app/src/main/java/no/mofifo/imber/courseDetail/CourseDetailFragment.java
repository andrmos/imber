package no.mofifo.imber.courseDetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.mofifo.imber.ImberApplication;
import no.mofifo.imber.R;
import no.mofifo.imber.fragments.AnnouncementFragment;
import no.mofifo.imber.listeners.MainActivityListener;

import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDetailFragment extends Fragment implements CourseDetailView {

    private static final String TAG = "CourseFragment";

    @Inject
    CourseDetailPresenter presenter;

    // TODO: 19.11.17 Init in onAttach
    MainActivityListener mCallback;

    @BindView(R.id.progressBar)
    SmoothProgressBar progressBar;

    @BindView(R.id.announcement1)
    TextView announcement1;

    @BindView(R.id.announcement2)
    TextView announcement2;

    @BindView(R.id.announcement3)
    TextView announcement3;

    @BindView(R.id.event1)
    TextView event1;

    @BindView(R.id.event2)
    TextView event2;

    @BindView(R.id.event3)
    TextView event3;

    @BindString(R.string.error_announcements_list)
    String coursesErrorMessage;

    @BindString(R.string.snackbar_retry_text)
    String retryButtonText;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_detail, container, false);
        ButterKnife.bind(this, rootView);
        presenter.onCreateView();
        return rootView;
    }

    @Override
    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void displayAnnouncements(@NonNull List<String> announcementTitles) {
        announcement1.setText(announcementTitles.get(0));
        announcement2.setText(announcementTitles.get(1));
        announcement3.setText(announcementTitles.get(2));
    }

    @Override
    public void displayAnnouncementsError() {
        // TODO: 21.11.17 Verify
        Snackbar snackbar = Snackbar.make(getView(), coursesErrorMessage, Snackbar.LENGTH_LONG);
        snackbar.setAction(retryButtonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadAnnouncements();
            }
        });
    }

    @Override
    public void displayEvents(@NonNull List<String> eventTitles) {
        event1.setText(eventTitles.get(0));
        event2.setText(eventTitles.get(1));
        event3.setText(eventTitles.get(2));
    }

    @Override
    public void displayEventsError() {
        // TODO: 21.11.17 Verify
        mCallback.showSnackbar(getString(R.string.error_requesting_events), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadEvents();
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

    @OnClick(R.id.announcement_card)
    public void onClickAnnouncementCard() {
        presenter.showAnnouncements();
    }

    @Override
    public void showAnnouncementsUi(int courseId, String courseCode) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        AnnouncementFragment announcementFragment = new AnnouncementFragment();
        transaction.replace(R.id.content_frame, announcementFragment);
        transaction.addToBackStack(null);

        Bundle args = new Bundle();
        args.putInt("courseId", courseId);
        args.putString("courseCode", courseCode);
        announcementFragment.setArguments(args);

        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick(R.id.events_card)
    public void onClickEventsCard() {
//                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    mCallback.initCalendar();
    }

    @OnClick(R.id.files_card)
    public void onClickFilesCard() {
//                    loadFileBrowserFragment();
    }

}

//    private void loadFileBrowserFragment() {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        FileBrowserFragment fileBrowserFragment = new FileBrowserFragment();
//        transaction.replace(R.id.content_frame, fileBrowserFragment);
//        transaction.addToBackStack(null);
//
//        Bundle args = new Bundle();
//        String json = new Gson().toJson(course);
//        args.putString("course", json);
//        fileBrowserFragment.setArguments(args);
//
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }


    /**
     * @return Returns true if all data is loaded
     */
//    private boolean isLoaded() {
//        for (int i = 0; i < loaded.length; i++) {
//            if (!loaded[i]) return false;
//        }
//        return true;
//    }

    //    private void requestAssignments() {
//        //Course ids for context_codes in url
//        ArrayList<String> contextCodes = new ArrayList<>();
//        contextCodes.add("course_" + course.getId());
//
//        //Type - event/assignment
//        String type = "assignment";
//
//        //Only 3 agendas for one course
//        int perPage = 3;
//
//        //Get todays date in right format
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar cal = Calendar.getInstance();
//        String startDate = df.format(cal.getTime());
//        String endDate = df.format(cal.getTime());
//
//        Call<List<CalendarEvent>> call = mittUibClient.getEvents(startDate, endDate, contextCodes, null, type, perPage);
//        call.enqueue(new Callback<List<CalendarEvent>>() {
//            @Override
//            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {
//
//                if (isAdded()) {
//                    if (response.isSuccessful()) {
//
////                        agendas.addAll(response.body());
//
//                        loaded[2] = true;
//
//                        if (isLoaded()) {
//                            recyclerView.setVisibility(View.VISIBLE);
//                            progressBar.progressiveStop();
//                        }
//                    } else {
//                        progressBar.progressiveStop();
//                        mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                requestAssignments();
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
//                if (isAdded()) {
//                    mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            requestAssignments();
//                        }
//                    });
//                }
//            }
//        });
//    }
