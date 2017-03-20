package com.cooldevs.erasmuskit;

/**
 * Created by mario on 04/03/2017
 */

public class City {
    private String key;
    private String name;
    private String country;
    private long timestamp;

    public City() {
        this.timestamp = System.currentTimeMillis();
    }

    public City(String name, String country) {
        this.name = name;
        this.country = country;
        this.timestamp = System.currentTimeMillis();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
