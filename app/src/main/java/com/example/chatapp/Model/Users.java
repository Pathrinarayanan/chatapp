package com.example.chatapp.Model;

public class Users {
    String username;
    String imageURL;
    String id;


    String status;

    public Users(String username, String imageURL, String id) {
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
    }
    public Users(){

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

}
