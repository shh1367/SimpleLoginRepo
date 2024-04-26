package com.example.simplelogin.viewmodel;

import com.example.simplelogin.model.User;

public class UserModel {
    private String phoneNumber ;

    private String phoneNumberHint;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserModel(User user){
        this.phoneNumberHint = user.phoneNumberHint;

    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumberHint() {
        return phoneNumberHint;
    }
    public void setPhoneNumberHint(String phoneNumberHint) {
        this.phoneNumberHint = phoneNumberHint;
    }




}
