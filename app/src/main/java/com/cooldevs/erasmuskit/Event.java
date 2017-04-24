package com.cooldevs.erasmuskit;

/**
 * Created by mario on 23/04/2017
 */

public class Event extends Post {
    private String placeID;
    private long eventTimestamp;

    public Event() {

    }

    public Event(String title, String content, String city, long timestamp, String placeID, long eventTimestamp) {
        super(title, content, city, timestamp);
        this.placeID = placeID;
        this.eventTimestamp = eventTimestamp;
    }

    public String getPlaceID() {
        return placeID;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }
}
