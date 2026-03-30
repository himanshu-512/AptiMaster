package com.aptitudeapp.backend.dto;

public class TopicStatsDTO {

    private String subtopic;

    private int totalQuestions;
    private int solvedQuestions;

    private double accuracy;
    private double progress;

    private String level;

    // 🔹 Default Constructor
    public TopicStatsDTO() {}

    // 🔹 Getters & Setters

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getSolvedQuestions() {
        return solvedQuestions;
    }

    public void setSolvedQuestions(int solvedQuestions) {
        this.solvedQuestions = solvedQuestions;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}