package com.app.android_machine_monitoring_mobile.shared.user;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String uid;
    private String email;
    private String fullName;
    private String nickname;
    private String mobilePhoneNumber;
    private String userProfilePictureUrl;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String email, String fullName, String nickname,
                String mobilePhoneNumber, String userProfilePictureUrl) {
        this.uid = uid;
        this.email = email;
        this.fullName = fullName;
        this.nickname = nickname;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.userProfilePictureUrl = userProfilePictureUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getUserProfilePictureUrl() {
        return userProfilePictureUrl;
    }

    public void setUserProfilePictureUrl(String userProfilePictureUrl) {
        this.userProfilePictureUrl = userProfilePictureUrl;
    }
}
