package com.flickrgallerytest.jobs.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.flickrgallerytest.jobs.Job;


public abstract class BasePreferencesJob implements Job {

    private Context mContext;

    public BasePreferencesJob(Context context) {
        mContext = context;
    }

    @Override
    public void doJob() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                "com.flickrgallerytest", Context.MODE_PRIVATE);
        doJob(sharedPreferences);
    }

    protected abstract void doJob(SharedPreferences sharedPreferences);
}
