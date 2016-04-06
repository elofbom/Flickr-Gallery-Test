package com.flickrgallerytest.jobs.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.flickrgallerytest.jobs.Job;

public class ReadPreferencesJob extends BasePreferencesJob {

    private Job.ResultCallback<String> mCallback;

    public ReadPreferencesJob(Context context, Job.ResultCallback<String> callback) {
        super(context);
        mCallback = callback;
    }

    @Override
    protected void doJob(SharedPreferences sharedPreferences) {
        String key = "com.flickrgallerytest.latest";

        String latestSearch = sharedPreferences.getString(key, "");
        if (!latestSearch.isEmpty()) {
            mCallback.onResult(latestSearch);
        }
        else {
            mCallback.onError();
        }
    }
}
