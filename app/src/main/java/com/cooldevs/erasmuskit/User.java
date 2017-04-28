package com.cooldevs.erasmuskit;

/**
 * Created by maite on 7/03/17
 */

public class User {
    private String key;
    private String userName;
    private String userEmail;
    private String nationality;
    private String studyField;
    private String hostCity;
    private String userType;
    private String userFacebookLink;
    private String userPicture;

    public User() {

    }

    public enum UserType {
        STUDENT("Student"),
        ORGANIZATOR("Organizator"),
        ADMINISTRATOR("Administrator");

        private String userType;

        UserType(String userType) {
            this.userType = userType;
        }

        public String getUserType() {
            return userType;
        }
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

    public String getUserFacebookLink() {
        return userFacebookLink;
    }

    public String getUserPicture() {
        return userPicture;
    }
}
