package com.flickrgallerytest.models;


public class Photo {

    private long id;
    private String owner;
    private String secret;
    private int server;
    private String title;
    private int farm;

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public int getServer() {
        return server;
    }

    public String getTitle() {
        return title;
    }

    public int getFarm() {
        return farm;
    }
}
