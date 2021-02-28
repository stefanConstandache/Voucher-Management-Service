package com.company;

import java.util.Vector;

public class User {
    private int User_ID;
    private String name;
    private String email;
    private String password;

    enum UserType{
        ADMIN,GUEST
    }

    private UserType uType;
    private UserVoucherMap userVoucherMap;
    private Vector<Notification> notifications;

    public User(int id, String nume, String pass, String em, UserType t){
        this.User_ID = id;
        this.name = nume;
        this.email = em;
        this.password = pass;
        this.uType = t;
        this.notifications = new Vector<Notification>();
    }

    public int getUser_ID() {
        return User_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getuType() {
        return uType;
    }

    public void setuType(UserType uType) {
        this.uType = uType;
    }

    public UserVoucherMap getUserVoucherMap() {
        return userVoucherMap;
    }

    public void setUserVoucherMap(UserVoucherMap userVoucherMap) {
        this.userVoucherMap = userVoucherMap;
    }

    public Vector<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Vector<Notification> notifications) {
        this.notifications = notifications;
    }

    public String toString(){
        return "[" + User_ID + ";" + name + ";" + email + ";" + uType + "]";
    }
}
