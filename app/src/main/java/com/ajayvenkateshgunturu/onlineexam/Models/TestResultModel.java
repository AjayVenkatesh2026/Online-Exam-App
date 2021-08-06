package com.ajayvenkateshgunturu.onlineexam.Models;

public class TestResultModel {
    String Uid, mail;
    int score, noOfQuestions;

    public TestResultModel() {
    }

    public TestResultModel(String uid, String mail, int score, int noOfQuestions) {
        Uid = uid;
        this.mail = mail;
        this.score = score;
        this.noOfQuestions = noOfQuestions;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }
}
