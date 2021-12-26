package com.example.chatapp.Login;

public class AddUser {
    String email,username,tag;
    int connections;

    public AddUser()
    {

    }

    public AddUser(String email,String username,String tag)
    {
        this.email=email;
        this.username=username;
        this.tag=tag;
        this.connections=0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }
}
