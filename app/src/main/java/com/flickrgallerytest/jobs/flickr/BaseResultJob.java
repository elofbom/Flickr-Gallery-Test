package com.flickrgallerytest.jobs.flickr;


import com.flickrgallerytest.jobs.Job;

public abstract class BaseResultJob<T> implements Job {

    private ResultCallback<T> mCallback;

    public BaseResultJob(ResultCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void doJob() {
        doJob(mCallback);
    }

    protected abstract void doJob(ResultCallback<T> callback);
}
