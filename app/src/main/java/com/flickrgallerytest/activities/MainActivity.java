package com.flickrgallerytest.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.flickrgallerytest.R;
import com.flickrgallerytest.adapters.SearchResultAdapter;
import com.flickrgallerytest.jobs.Job;
import com.flickrgallerytest.jobs.flickr.RetrieveImagesSearchJob;
import com.flickrgallerytest.jobs.shared_preferences.ReadPreferencesJob;
import com.flickrgallerytest.jobs.shared_preferences.SavePreferencesJob;
import com.flickrgallerytest.models.Photo;
import com.flickrgallerytest.models.Root;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mUrls;
    private SearchResultAdapter mSearchResultAdapter;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
    private CoordinatorLayout mCoordinatorLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_grid_view);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_container);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progressbar);

        mUrls = new ArrayList<>();
        mSearchResultAdapter = new SearchResultAdapter(mUrls, this);

        mRecyclerView.setHasFixedSize(true);

        int columnSize = calculateColumnSize();
        mGridLayoutManager = new GridLayoutManager(this, columnSize);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mSearchResultAdapter);

        retrievePreviousSearchedImages();
    }

    private int calculateColumnSize() {
        int columns = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 5;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            columns = 3;
        }
        return columns;
    }

    private void retrievePreviousSearchedImages() {
        mProgressBar.setVisibility(View.VISIBLE);
        Job readPreferencesJob = new ReadPreferencesJob(this, new Job.ResultCallback<String>() {
            @Override
            public void onResult(String data) {
                retrieveImages(data);
                getSupportActionBar().setTitle(data);
            }

            @Override
            public void onError() {
                mProgressBar.setVisibility(View.GONE);
                Snackbar.make(mCoordinatorLayout, "No previous search", Snackbar.LENGTH_LONG).show();
            }
        });
        readPreferencesJob.doJob();
    }

    private void retrieveImages(final String search) {
        mProgressBar.setVisibility(View.VISIBLE);
        Job retrieveImagesSearchJob = new RetrieveImagesSearchJob(search, new Job.ResultCallback<String>() {
            @Override
            public void onResult(String data) {
                handleResult(data, search);
            }

            @Override
            public void onError() {
                mProgressBar.setVisibility(View.GONE);
                Snackbar.make(mCoordinatorLayout, "Can not connect", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        retrieveImages(search);
                    }
                }).show();
            }
        });
        retrieveImagesSearchJob.doJob();
    }

    private void handleResult(String data, final String search) {
        Gson gson = new GsonBuilder().create();
        Root root = gson.fromJson(data, Root.class);

        mUrls.clear();

        for (Photo photo : root.getPhotos().getPhoto()) {
            String url = "https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + ".jpg";
            mUrls.add(url);
        }
        updateDataOnUiThread(search);
    }

    /**
     * Update the search data on the main thread. This has to be run on the Ui thread because we will modify views.
     */
    private void updateDataOnUiThread(final String search) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchResultAdapter.notifyDataSetChanged();
                getSupportActionBar().setTitle(search);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_main_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Job savePreferencesJob = new SavePreferencesJob(MainActivity.this, query);
                savePreferencesJob.doJob();

                retrieveImages(query);

                searchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager.setSpanCount(5);
            mRecyclerView.setLayoutManager(mGridLayoutManager);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager.setSpanCount(3);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        }
    }
}
