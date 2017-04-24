package com.cooldevs.erasmuskit;

/**
 * Created by mario on 23/04/2017
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
        EVENT("Event", "events", Event.class),
        TIP("Tip", "tips", Event.class), //TODO: Create Tip Class and change this!!!
        PLACE("Place", "places", Event.class); //TODO: Create Place Class and change this!!!

        private String postType;
        private String dbRef;
        private Class<Post> mClass;

        PostType(String postType, String dbRef, Class mClass) {
            this.postType = postType;
            this.dbRef = dbRef;
            this.mClass = mClass;
        }

        public String getPostType() {
            return postType;
        }

        public String getDbRef() {
            return dbRef;
        }

        public Class<Post> getmClass() {
            return mClass;
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