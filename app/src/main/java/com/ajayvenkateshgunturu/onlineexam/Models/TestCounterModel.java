package com.ajayvenkateshgunturu.onlineexam.Models;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestCounterModel {

    private int year, month, day, hour, minutes, duration;
    private Calendar targetDate;
    private long differenceInTime;

    public TestCounterModel() {
    }

    public TestCounterModel(int year, int month, int day, int hour, int minutes, int duration) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.duration = duration;

        targetDate = Calendar.getInstance();
        targetDate.set(year, month, day, hour, minutes, 0);

        calculateDifferenceInTime();
        Date d = new Date();
    }

    private void calculateDifferenceInTime() {
        Date d = new Date();
        differenceInTime = targetDate.getTime().getTime() - d.getTime();
    }

    public long differenceInDays(){
        calculateDifferenceInTime();
        return TimeUnit.MILLISECONDS.toDays(differenceInTime);
    }


    public long differenceInHours(){
        calculateDifferenceInTime();
        return TimeUnit.MILLISECONDS.toHours(differenceInTime) % 24;
    }

    public long differenceInMinutes(){
        calculateDifferenceInTime();
        return TimeUnit.MILLISECONDS.toMinutes(differenceInTime) % 60;
    }

    public boolean isItTimeForTest(){
        calculateDifferenceInTime();
        long dur = TimeUnit.MINUTES.toMillis(duration) * -1;
        if(differenceInTime <= 0 && differenceInTime >= dur){
            return true;
        }
        return false;
    }

    public boolean isThereTimeUntilTest(){
        calculateDifferenceInTime();
        if(differenceInTime > 0){
            return true;
        }
        return false;
    }

    public boolean isTimeUp(){
        calculateDifferenceInTime();
        long dur = TimeUnit.MINUTES.toMillis(duration) * -1;
        if(differenceInTime < dur){
            return true;
        }
        return false;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
