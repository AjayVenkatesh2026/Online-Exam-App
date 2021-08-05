package com.ajayvenkateshgunturu.onlineexam.Models;

import android.os.Bundle;

public class TestHeaderModel {

    String title, description, date, time, duration, testId;

    public TestHeaderModel() {
    }

    public TestHeaderModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public TestHeaderModel(String title, String description, String date, String time, String duration) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putString("title", this.getTitle());
        b.putString("description", this.getDescription());
        b.putString("date", this.getDate());
        b.putString("time", this.getTime());
        b.putString("duration", this.getDuration());
        b.putString("testId", this.getTestId());
        return b;
    }

    public static TestHeaderModel fromBundle(Bundle bundle){
        TestHeaderModel header =  new TestHeaderModel(bundle.getString("title"), bundle.getString("description"),
                bundle.getString("date"), bundle.getString("time"), bundle.getString("duration"));
        header.setTestId(bundle.getString("testId"));
        return header;
    }
}
