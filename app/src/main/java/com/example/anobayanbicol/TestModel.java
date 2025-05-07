package com.example.anobayanbicol;

public class TestModel {

    private String testID;
    private int topScore;
    private int time;

    public TestModel(String testID, int topScore, int time) {
        this.topScore = topScore;
        this.testID = testID;
        this.time = time;

    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
