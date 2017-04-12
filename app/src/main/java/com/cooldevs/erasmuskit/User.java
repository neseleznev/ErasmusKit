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

    public User(){

    }

    public User(String hostCity, String userName, String userEmail, String nationality, String studyField, String userType){
        this.hostCity=hostCity;
        this.nationality=nationality;
        this.studyField=studyField;
        this.userName=userName;
        this.userType=userType;
        this.userEmail=userEmail;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getNationality() {

        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getStudyField() {
        return studyField;
    }

    public void setStudyField(String studyField) {
        this.studyField = studyField;
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

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
