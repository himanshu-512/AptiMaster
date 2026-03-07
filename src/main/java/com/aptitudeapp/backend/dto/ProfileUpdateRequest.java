package com.aptitudeapp.backend.dto;

public class ProfileUpdateRequest {

    private String name;
    private Integer age;
    private String email;
    private String avatar;

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
}
