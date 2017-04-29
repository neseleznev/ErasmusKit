package com.cooldevs.erasmuskit;

/**
 * Created by mario on 23/04/2017
 */

public class Event extends Post {
    private String placeID;
    private String placeName;
    private long eventTimestamp;
    private String facebookLink;

    public Event() {

    }

    public Event(String title, String content, String city, long timestamp, String placeID, String placeName, long eventTimestamp, String facebookLink) {
        super(title, content, city, timestamp);
        this.placeID = placeID;
        this.placeName = placeName;
        this.eventTimestamp = eventTimestamp;
        this.facebookLink = facebookLink;
    }

    @Override
    public PostType getPostType() {
        return PostType.EVENT;
    }

    public String getPlaceID() {
        return placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public String getFacebookLink() {
        return facebookLink;
    }
}
