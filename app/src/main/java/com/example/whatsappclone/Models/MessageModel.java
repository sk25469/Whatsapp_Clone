package com.example.whatsappclone.Models;

public class MessageModel {
    String messageId, senderId, message;

    public MessageModel(String messageId, String senderId, String message){
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }
}
