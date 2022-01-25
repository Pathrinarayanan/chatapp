package com.example.chatapp.Model;

public class Users {
    String username;
    String imageURL;
    String id;
    String search;
    String frid;


    String status;

    public Users(String username, String imageURL, String id, String status ,String search,String frid) {
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
        this.search = search;
        this.frid = frid;
    }
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
