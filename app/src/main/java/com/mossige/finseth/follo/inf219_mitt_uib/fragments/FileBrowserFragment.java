package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.FileBrowserRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.EndlessRecyclerViewScrollListener;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.File;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Folder;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PaginationUtils;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.CancelableCallback;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;
import com.mossige.finseth.follo.inf219_mitt_uib.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by andre on 09.01.2017.
 */
public class FileBrowserFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "FileBrowserFragment";

    private static final String CURRENT_FOLDER_ID_KEY = "currentFolderId";
    public static final String COURSE_KEY = "course";
    // If file size is above this limit, prompt for confirmation: 30 MB
    private static final long FILE_SIZE_WARNING_TRESHHOLD = 30000000;

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<File> files;
    private ArrayList<Folder> folders;

    private Course course;
    private MainActivityListener callback;

    private String nextPageFiles;
    private String nextPageFolders;

    // The id of the folder currently being browsed.
    private int currentFolderId;
    private DownloadComplete downloadComplete;
    private long enqueuedDownload;
    private File clickedFile;

    public FileBrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callback = (MainActivityListener) context;
        } catch (ClassCastException e) {
            //Do nothing
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initArguments();
        initBroadcastReceiver();
    }

    private void initArguments() {
        if (getArguments() != null) {
            // The course whose files are being viewed
            if (getArguments().containsKey(COURSE_KEY)) {
                String json = getArguments().getString(COURSE_KEY);
                course = new Gson().fromJson(json, Course.class);
            }

            // If no current folder id argument exists, we are in root folder
            if (getArguments().containsKey(CURRENT_FOLDER_ID_KEY)) {
                currentFolderId = getArguments().getInt(CURRENT_FOLDER_ID_KEY);
                getFolders(currentFolderId);
                getFiles(currentFolderId);
            } else {
                getRootFolder();
            }
        }
    }

    private void initVariables() {
        files = new ArrayList<>();
        folders = new ArrayList<>();
        nextPageFiles = "";
        nextPageFolders = "";
    }

    private void initBroadcastReceiver() {
        // Register broadcastreceiver for finished download
        downloadComplete = new DownloadComplete();
        getContext().registerReceiver(downloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void getRootFolder() {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
        Call<Folder> call = client.getRootFolder(course.getId());
        call.enqueue(new CancelableCallback<Folder>() {
            @Override
            public void onSuccess(Call<Folder> call, Response<Folder> response) {

                if (response.isSuccessful()) {
                    currentFolderId = response.body().getId();
                    getFolders(currentFolderId);
                    getFiles(currentFolderId);
                } else {
                    showSnackbarRoot();
                }

            }

            @Override
            public void onError(Call<Folder> call, Throwable t) {
                showSnackbarRoot();
            }
        });
    }

    private void showSnackbarRoot() {
        callback.showSnackbar(getString(R.string.error_getting_folders), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRootFolder();
            }
        });
    }

    private void getFolders(final int folderId) {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());

        Call<List<Folder>> call;
        boolean firstPage = nextPageFolders.isEmpty();
        if (firstPage) {
            call = client.getFolders(folderId);
        } else {
            call = client.getFoldersPaginate(nextPageFolders);
        }

        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.isSuccessful()) {
                    int currentSize = folders.size();
                    folders.addAll(response.body());

                    mAdapter.notifyItemRangeInserted(currentSize, response.body().size());

                    nextPageFolders = PaginationUtils.getNextPageUrl(response.headers());
                } else {
                    showSnackbarFolder(folderId);
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                showSnackbarFolder(folderId);
            }
        });

    }

    private void getFiles(final int folderId) {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());

        Call<List<File>> call;
        boolean firstPage = nextPageFiles.isEmpty();
        if (firstPage) {
            call = client.getFilesByFolder(folderId, null);
        } else {
            call = client.getFilesPaginate(nextPageFiles);
        }

        call.enqueue(new Callback<List<File>>() {
            @Override
            public void onResponse(Call<List<File>> call, Response<List<File>> response) {
                if (response.isSuccessful()) {
                    int currentSize = mAdapter.getItemCount();
                    files.addAll(response.body());
                    mAdapter.notifyItemRangeInserted(currentSize, response.body().size());

                    nextPageFiles = PaginationUtils.getNextPageUrl(response.headers());
                } else {
                    showSnackbarFile(folderId);
                }
            }

            @Override
            public void onFailure(Call<List<File>> call, Throwable t) {
                showSnackbarFile(folderId);
            }
        });
    }

    private void showSnackbarFolder(final int folderId) {
        callback.showSnackbar(getString(R.string.error_getting_folders), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFolders(folderId);
            }
        });
    }

    private void showSnackbarFile(final int folderId) {
        callback.showSnackbar(getString(R.string.error_getting_files), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFiles(folderId);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file_browser, container, false);
        getActivity().setTitle(R.string.btn_file_browser);

        initRecycleView(rootView);
        mainList.setVisibility(View.VISIBLE);

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainList.setVisibility(View.GONE);

        // Create the LayoutManager that holds all the views
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(layoutManager);

        mainList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO Solve properly
                if (!nextPageFolders.isEmpty()) {
                    getFolders(currentFolderId);
                }
                if (!nextPageFiles.isEmpty()) {
                    getFiles(currentFolderId);
                }
            }
        });

        // Create adapter that binds the views with some content
        mAdapter = new FileBrowserRecyclerViewAdapter(files, folders);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (position < folders.size()) {
                    handleFolderClick(position);

                } else {
                    handleFileClick(position);
                }
            }
        });
    }

    private void handleFileClick(int position) {
        clickedFile = files.get(position - folders.size());
        downloadFile(clickedFile);
    }

    private void handleFolderClick(int position) {
        Folder clicked = folders.get(position);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        FileBrowserFragment fileBrowserFragment = new FileBrowserFragment();
        transaction.replace(R.id.content_frame, fileBrowserFragment);

        transaction.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_FOLDER_ID_KEY, clicked.getId());
        fileBrowserFragment.setArguments(bundle);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getContext() != null && downloadComplete != null) {
            getContext().unregisterReceiver(downloadComplete);
        }
    }

    // TODO Download file to cache folder instead of external?
    public void downloadFile(File file) {

        // File size in bytes
        if (file.getSize() > FILE_SIZE_WARNING_TRESHHOLD) {
            // TODO Implement

        } else {
            DownloadManager manager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(file.getUrl()));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getFileName());

            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (PermissionUtils.isPermissionGranted(permission, getActivity())) {
                enqueuedDownload = manager.enqueue(request);

            } else {
                requestPermissions(new String[]{permission}, 1);
            }
        }


    }

    private class DownloadComplete extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                viewFile();
            }
        }
    }

    private void viewFile() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(enqueuedDownload);
        DownloadManager manager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);

        Uri uri = lastSuccessfulDownloadUri(manager, query);
        if (uri != null) {
            startActivity(uri);
        }
    }

    private Uri lastSuccessfulDownloadUri(DownloadManager manager, DownloadManager.Query query) {
        // Get URI if latest successfully downloaded file
        Cursor c = manager.query(query);
        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                return Uri.parse(uriString);
            }
        }
        return null;
    }

    private void startActivity(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            downloadFile(clickedFile);
        }
    }
}
