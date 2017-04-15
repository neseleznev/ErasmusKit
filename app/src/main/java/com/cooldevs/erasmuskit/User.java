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

    public User(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public User(String hostCity, String userName, String userEmail, String nationality, String studyField, String userType) {
        this.hostCity = hostCity;
        this.nationality = nationality;
        this.studyField = studyField;
        this.userName = userName;
        this.userType = userType;
        this.userEmail = userEmail;
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

    public void setHostCity(String hostCity) {
        this.hostCity = hostCity;
    }

    public String getUserType() {
        return userType;
    }
}
