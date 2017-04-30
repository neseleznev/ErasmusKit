package com.cooldevs.erasmuskit.ui.posts.model;

/**
 * Created by mario on 27/04/2017
 */

public class Place extends Post {
    private float rating;
    private String placeID;

    public Place() {

    }

    public Place(String title, String content, String city, long timestamp, float rating, String placeID) {
        super(title, content, city, timestamp);
        this.rating = rating;
        this.placeID = placeID;
    }

    @Override
    public PostType getPostType() {
        return PostType.PLACE;
    }

    public float getRating() {
        return rating;
    }

    public String getPlaceID() {
        return placeID;
    }
}
