package com.example.whatsappclone.Models;

public class UserModel {
    private String name, phone, uid, notificationKey;

    public UserModel(String uid) {
        this.uid = uid;
    }

    public UserModel(String uid, String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
