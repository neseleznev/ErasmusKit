package com.cooldevs.erasmuskit;

/**
 * Created by mario on 17/04/2017.
 */

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
