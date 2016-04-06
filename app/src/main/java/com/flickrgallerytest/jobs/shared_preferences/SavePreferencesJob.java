package com.flickrgallerytest.jobs.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePreferencesJob extends BasePreferencesJob {

    private String mSearch;

    public SavePreferencesJob(Context context, String search) {
        super(context);
        mSearch = search;
    }

    @Override
    protected void doJob(SharedPreferences sharedPreferences) {
        String key = "com.flickrgallerytest.latest";
        sharedPreferences.edit().putString(key, mSearch).apply();
    }
}
