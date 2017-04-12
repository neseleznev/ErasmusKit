package com.cooldevs.erasmuskit;

/**
 * Created by maite on 12/04/17.
 */

public class ProfileSection {
    private int icon;
    private String value;

    public ProfileSection(int icon, String value) {
        this.icon = icon;
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
