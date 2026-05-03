package com.aptitudeapp.backend.dto;

import java.util.List;

public class ProfileUpdateRequest {

    private String name;
    private Integer age;
    private String email;
    private String avatar;
    private String examGoal;
    private String target;
    private Integer dailyGoal;
    private List<String> preferredTopics;

    public ProfileUpdateRequest() {
    }

    public ProfileUpdateRequest(String name, Integer age, String avatar, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getExamGoal() {
        return examGoal;
    }

    public void setExamGoal(String examGoal) {
        this.examGoal = examGoal;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(Integer dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public List<String> getPreferredTopics() {
        return preferredTopics;
    }

    public void setPreferredTopics(List<String> preferredTopics) {
        this.preferredTopics = preferredTopics;
    }
}
