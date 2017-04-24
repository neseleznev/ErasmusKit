package com.cooldevs.erasmuskit;

/**
 * Created by maite on 12/04/17
 */

class Section {
    private int icon;
    private String title;

    Section(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
