package com.ajayvenkateshgunturu.onlineexam.Models;

public class TestQuestionModel {
    String question, optionOne, optionTwo, optionThree, optionFour;
    int numOfOptions;
    String ans;

    public TestQuestionModel(){

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

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public int getNumOfOptions() {
        return numOfOptions;
    }

    public void setNumOfOptions(int numOfOptions) {
        this.numOfOptions = numOfOptions;
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

}
