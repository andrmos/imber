package no.mofifo.imber.course;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import no.mofifo.imber.adapters.CourseListRecyclerViewAdapter;
import no.mofifo.imber.R;
import no.mofifo.imber.data.MittUibRepository;
import no.mofifo.imber.fragments.CourseDetailFragment;
import no.mofifo.imber.listeners.EndlessRecyclerViewScrollListener;
import no.mofifo.imber.listeners.ItemClickSupport;
import no.mofifo.imber.listeners.MainActivityListener;
import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.PaginationUtils;
import no.mofifo.imber.retrofit.MittUibClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A placeholder fragment containing a simple view.
 */
public class CoursesFragment extends Fragment implements CoursesView {

    private static final String TAG = "CourseListFragment";

    @BindView(R.id.recycler_view) RecyclerView mainList;
    @BindView(R.id.progressbar) SmoothProgressBar progressBar;

    @BindString(R.string.error_course_list) String errorMessageCourses;

    private RecyclerView.Adapter mAdapter;

    private ArrayList<Course> courses;
    /* If data is loaded */
    private boolean loaded;

    MainActivityListener mCallback;

    // TODO remove client as it will not be used any more
    @Inject
    MittUibClient mittUibClient;

    private String nextPage;

    // TODO Inject the presenter via dagger
    /** This fragments presenter */
    CoursesPresenter presenter;

    public CoursesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextPage = "";
        loaded = false;
        courses = new ArrayList<>();

        ((ImberApplication) getActivity().getApplication()).getApiComponent().inject(this);

        // TODO Use dagger in this instantiations
        // Manual dependency injection:
        presenter = new CoursesPresenter(this, new MittUibRepository(mittUibClient));

        requestCourses();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainActivityListener) context;
        }catch (ClassCastException e){
            Log.i(TAG, "onAttach: " + e.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);
        getActivity().setTitle(R.string.course_title);

        initRecyclerView();

        // Hide progress bar if data is already loaded
        if (loaded) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initRecyclerView() {
        mainList.setVisibility(View.VISIBLE);

        // Create the LayoutManager that holds all the views
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        mainList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // If there is a next link
                if (!nextPage.isEmpty()) {
                    requestCourses();
                }
            }
        });

        // Create adapter that binds the views with some content
        mAdapter = new CourseListRecyclerViewAdapter(courses);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CourseDetailFragment courseDetailFragment = new CourseDetailFragment();
                transaction.replace(R.id.content_frame, courseDetailFragment);

                transaction.addToBackStack(null);

                Bundle bundle = new Bundle();
                String json = new Gson().toJson(courses.get(position));
                bundle.putString("course", json);
                courseDetailFragment.setArguments(bundle);
                transaction.commit();
            }
        });
    }

    private void requestCourses() {
        Call<List<Course>> call;
        boolean firstPage = nextPage.isEmpty();
        if (firstPage) {
            call = mittUibClient.getFavoriteCourses();
        } else {
            call = mittUibClient.getCoursesPagination(nextPage);
        }

        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    if (progressBar != null) {
                        progressBar.progressiveStop();
                    }

                    courses.addAll(response.body());
                    nextPage = PaginationUtils.getNextPageUrl(response.headers());

                    loaded = true;
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                if (isAdded()) {
                    if (progressBar != null){
                        progressBar.progressiveStop();
                    }

                    mCallback.showSnackbar(errorMessageCourses, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestCourses();
                        }
                    });
                }
            }
        });

    }
}
