package no.mofifo.imber.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import no.mofifo.imber.ImberApplication;
import no.mofifo.imber.R;
import no.mofifo.imber.fragments.CourseDetailFragment;
import no.mofifo.imber.listeners.EndlessRecyclerViewScrollListener;
import no.mofifo.imber.listeners.ItemClickSupport;
import no.mofifo.imber.models.Course;

import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Fragment containing a list of courses.
 * Clicking on a course loads a new fragment with details of the clicked course.
 */
public class CoursesFragment extends Fragment implements CoursesView, ItemClickSupport.OnItemClickListener {

    private static final String TAG = "CoursesFragment";

    @BindView(R.id.recycler_view)
    RecyclerView mainList;
    @BindView(R.id.progressbar)
    SmoothProgressBar progressBar;

    @BindString(R.string.error_course_list)
    String coursesErrorMessage;
    @BindString(R.string.snackback_action_text)
    String retryButtonText;

    /* This fragments presenter */
    @Inject
    CoursesPresenter presenter;

    /* Adapter binding content to the recycler view */
    @Inject
    CoursesAdapter adapter;

    public CoursesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCoursesComponent.builder()
                .apiComponent(((ImberApplication) getActivity().getApplication()).getApiComponent())
                .coursesPresenterModule(new CoursesPresenterModule(this)).build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.course_title);
        initRecyclerView();

        // TODO View should be dumb. Replace with presenter.start() or similar
        presenter.loadFavoriteCourses();
        return rootView;
    }

    // TODO Fix method
    private void initRecyclerView() {
        mainList.setVisibility(View.VISIBLE);

        // Create the LayoutManager that holds all the views
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mainList.setLayoutManager(mLayoutManager);

        mainList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // If there is a next link
//                if (!nextPage.isEmpty()) {
//                    requestCourses();
//                }
            }
        });

        mainList.setAdapter(adapter);
        ItemClickSupport.addTo(mainList).setOnItemClickListener(this);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Course clicked = adapter.getCourse(position);
        presenter.onCourseClicked(clicked);
    }

    @Override
    public void showCourseDetails(Course course) {
        Bundle bundle = new Bundle();
        String json = new Gson().toJson(course);
        bundle.putString("course", json);

        CourseDetailFragment courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, courseDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void displayCourses(List<Course> courses) {
        adapter.addCourses(courses);
    }

    @Override
    public void displayCoursesError() {
        Snackbar snackbar = Snackbar.make(getView(), coursesErrorMessage, Snackbar.LENGTH_LONG);
        snackbar.setAction(retryButtonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadFavoriteCourses();
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
}
