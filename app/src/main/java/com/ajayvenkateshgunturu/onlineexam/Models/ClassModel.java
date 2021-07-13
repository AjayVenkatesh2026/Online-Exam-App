package com.ajayvenkateshgunturu.onlineexam.Models;

import android.util.Log;

public class ClassModel {
    String classId;
    String className;
    String classDescription;

    public ClassModel(){

    }

    public ClassModel(String classId, String className, String classDescription) {
        this.classId = classId;
        this.className = className;
        this.classDescription = classDescription;
    }

    public String getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public void log(){
        Log.e("Log of class Model", getClassName() + " " + getClassDescription());
    }

    @Override
    public String toString() {
        return "ClassModel{" +
                "classId='" + classId + '\'' +
                ", className='" + className + '\'' +
                ", classDescription='" + classDescription + '\'' +
                '}';
    }
}
