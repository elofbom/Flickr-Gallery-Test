package com.flickrgallerytest.jobs.flickr;


import com.flickrgallerytest.jobs.Job;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RetrieveImagesSearchJob extends BaseResultJob<String> {

    private String mSearch;

    public RetrieveImagesSearchJob(String search, Job.ResultCallback<String> callback) {
        super(callback);
        mSearch = search;
    }

    @Override
    protected void doJob(final Job.ResultCallback<String> callback) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("https://api.flickr.com/services/rest/?&method=flickr.photos.search&text=" + mSearch + "&api_key=d69347727bafb1b964c5e1e18c259d01&format=json&nojsoncallback=1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.onResult(response.body().string());
            }
        });
    }
}
