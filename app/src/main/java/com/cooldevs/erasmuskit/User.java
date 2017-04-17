package com.cooldevs.erasmuskit;

/**
 * Created by maite on 7/03/17.
 */

public class User {
    private String key;
    private String userName;
    private String userEmail;
    private String nationality;
    private String studyField;
    private String hostCity;
    private String userType;

    public User() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getNationality() {
        return nationality;
    }

    public String getStudyField() {
        return studyField;
    }

    public String getHostCity() {
        return hostCity;
    }

    public String getUserType() {
        return userType;
    }
}
