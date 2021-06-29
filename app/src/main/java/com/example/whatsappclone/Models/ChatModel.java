package com.example.whatsappclone.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatModel implements Serializable {
    private String chatId;

    private ArrayList<UserModel> userModelArrayList = new ArrayList<>();

    public ChatModel(String chatId){
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public ArrayList<UserModel> getUserModelArrayList() {
        return userModelArrayList;
    }

    public void addUserToArrayList(UserModel userModel){
        userModelArrayList.add(userModel);
    }
}
