package com.uc.nplc.model;

import java.io.Serializable;

public class Post implements Serializable {

    private String id;
    private String title;
    private String location;
    private String type;

    public Post(String id, String title, String location, String type) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
