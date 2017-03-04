package com.cooldevs.erasmuskit;

/**
 * Created by mario on 04/03/2017
 */

public class City {
    private String key;
    private String name;
    private String country;

    public City() {

    }

    public City(String name, String country) {
        this.name = name;
        this.country = country;
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
}
