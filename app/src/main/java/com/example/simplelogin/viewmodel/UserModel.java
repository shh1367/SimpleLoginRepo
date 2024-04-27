package com.example.simplelogin.viewmodel;

import androidx.databinding.BaseObservable;

import com.example.simplelogin.R;
import com.example.simplelogin.model.User;

public class UserModel extends BaseObservable {
    private String phoneNumber;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserModel(User user) {
        this.phoneNumber = user.phoneNumber;

    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(R.id.edt_phone);
    }


}
