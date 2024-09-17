package com.ms_ma_backend_test.ms_ma.entity;

import com.ms_ma_backend_test.ms_ma.dtos.LoginRequest;
import com.ms_ma_backend_test.ms_ma.dtos.SignUpRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message = "Username must be 5-10 alphanumeric characters with no special symbols.")
    private String username;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant signUpDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileVisibility profileVisibility = ProfileVisibility.PUBLIC;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "requester")
    private List<FriendShip> sentFriendships = new ArrayList<>();

    @OneToMany(mappedBy = "friend")
    private List<FriendShip> receivedFriendships = new ArrayList<>();

    private String badge;

    public enum ProfileVisibility {
        HIDDEN,
        PUBLIC
    }

    public User() {}

    public User(SignUpRequest request) {
        this.username = request.username();
        this.profileVisibility = request.profileVisibility();
        this.password = request.password();
    }

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

    public ProfileVisibility getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(ProfileVisibility profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FriendShip> getSentFriendships() {
        return sentFriendships;
    }

    public void setSentFriendships(List<FriendShip> sentFriendships) {
        this.sentFriendships = sentFriendships;
    }

    public List<FriendShip> getReceivedFriendships() {
        return receivedFriendships;
    }

    public void setReceivedFriendships(List<FriendShip> receivedFriendships) {
        this.receivedFriendships = receivedFriendships;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {

        return passwordEncoder.matches(loginRequest.password(), this.password);
    }

}

