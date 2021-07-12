package com.ajayvenkateshgunturu.onlineexam.Models;

import android.util.Log;

public class StudentModel {
    private String email, name, userType, Uid;

    public StudentModel(String email, String name, String userType, String uid) {
        this.email = email;
        this.name = name;
        this.userType = userType;
        Uid = uid;
    }

    public StudentModel() {
    }

    public StudentModel(String email, String name, String userType) {
        this.email = email;
        this.name = name;
        this.userType = userType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUserType() {
        return userType;
    }

    public String getUid() {
        return Uid;
    }

    public void log(){
        String log = "Uid: " + getUid()
                + "\n Name: " + getName()
                +"\n Email: " + getEmail()
                +"\n User Role: " + getUserType() + "\n";
        Log.e("Student\n" , log);
    }
}
