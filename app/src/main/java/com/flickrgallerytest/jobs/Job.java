package com.flickrgallerytest.jobs;


public interface Job {

    void doJob();

    interface ResultCallback<T> {

        void onResult(T data);
        void onError();
    }
}
