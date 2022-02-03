package com.example.chatapp.Model;

public class Users {
    String username;
   String imageURL;
    String id;
    String search;

    public Users(String username, String imageURL, String id, String search, String mood, String frid, String tag, String status) {
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
        this.search = search;
        this.mood = mood;
        this.frid = frid;
        this.tag = tag;
        this.status = status;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    String mood;
    String frid;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    String tag;


    String status;

    public Users(){

    }



    public String getFrid() {
        return frid;
    }

    public void setFrid(String frid) {
        this.frid = frid;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
