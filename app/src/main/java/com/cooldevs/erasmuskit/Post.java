package com.cooldevs.erasmuskit;

/**
 * Created by mario on 23/04/2017.
 */

public class Post {
    private String key;
    private String title;
    private String content;
    private String city;
    private long timestamp;

    public Post() {

    }

    public Post(String title, String content, String city, long timestamp) {
        this.title = title;
        this.content = content;
        this.city = city;
        this.timestamp = timestamp;
    }

    public enum PostType {
        EVENT("Event"),
        TIP("Tip"),
        PLACE("Place");

        private String postType;

        PostType(String postType) {
            this.postType = postType;
        }

        public String getPostType() {
            return postType;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCity() {
        return city;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
