package com.ms_ma_backend_test.ms_ma.dtos;

import com.ms_ma_backend_test.ms_ma.entity.User;

import java.time.Instant;

public class UserDto {


    private Long id;
    private String username;
    private Instant signUpDate;
    private User.ProfileVisibility profileVisibility;
    private String badge;

    public UserDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Instant signUpDate) {
        this.signUpDate = signUpDate;
    }

    public User.ProfileVisibility getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(User.ProfileVisibility profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
