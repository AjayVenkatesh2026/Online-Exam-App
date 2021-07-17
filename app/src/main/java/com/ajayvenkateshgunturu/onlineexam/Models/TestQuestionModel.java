package com.ajayvenkateshgunturu.onlineexam.Models;

public class TestQuestionModel {
    String question, optionOne, optionTwo, optionThree, optionFour;
    String type, title, description;
    int numOfOptions, priority;

    public TestQuestionModel(){

    }

    public TestQuestionModel(String title, String description) {
        this.title = title;
        this.description = description;
        type = "Header";
        this.priority = 0;
    }

    public TestQuestionModel(String question, String optionOne, String optionTwo, String optionThree, String optionFour) {
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;

        numOfOptions = 2;

        if(!optionThree.isEmpty()){
            numOfOptions = 3;
        }

        if(optionThree.isEmpty() && !optionFour.isEmpty()){
            numOfOptions = 3;
            this.optionFour = "";
            this.optionThree = optionFour;
        }

        if(!optionThree.isEmpty() && !optionFour.isEmpty()){
            numOfOptions = 4;
        }

        type = "Question";
        this.priority = 1;
    }

    public int getPriority() {
        return priority;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public int getNumOfOptions() {
        return numOfOptions;
    }

    public void setNumOfOptions(int numOfOptions) {
        this.numOfOptions = numOfOptions;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptionOne(String optionOne) {
        this.optionOne = optionOne;
    }

    public void setOptionTwo(String optionTwo) {
        this.optionTwo = optionTwo;
    }

    public void setOptionThree(String optionThree) {
        this.optionThree = optionThree;
        if(this.numOfOptions == 2){
            this.numOfOptions = 3;
        }
    }

    public void setOptionFour(String optionFour) {
        this.optionFour = optionFour;
        if(this.numOfOptions == 2){
            setOptionThree(optionFour);
        }
        else if(this.numOfOptions == 3){
            this.numOfOptions = 4;
        }
    }

    public void log(){
        System.out.println("Title: " + title + "\n"
        + "Description: " + description + "\n");
    }
}
