package com.ajayvenkateshgunturu.onlineexam.Models;

public class TestQuestionModel {
    String question, optionOne, optionTwo, optionThree, optionFour;
    int numOfOptions;

    public TestQuestionModel(String question, String optionOne, String optionTwo, String optionThree, String optionFour) {
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        numOfOptions = 4;
    }

    public TestQuestionModel(String question, String optionOne, String optionTwo, String optionThree) {
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        numOfOptions = 3;
    }

    public TestQuestionModel(String question, String optionOne, String optionTwo) {
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        numOfOptions = 2;
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
    }

    public void setOptionFour(String optionFour) {
        this.optionFour = optionFour;
    }

    public void setNumOfOptions(int numOfOptions) {
        this.numOfOptions = numOfOptions;
    }
}
