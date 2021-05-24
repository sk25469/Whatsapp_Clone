package com.example.whatsappclone.Models;

public class UserModel {
    private String name, phone;

    public UserModel(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
