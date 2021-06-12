package com.example.whatsappclone.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.MessageAdapter;
import com.example.whatsappclone.Models.ChatModel;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    ArrayList<MessageModel> messageList;

    String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");

        Button mSend = findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessge();
            }
        });

        initializeRecyclerView();
    }

    private void sendMessge(){
        EditText mMessage = findViewById(R.id.message);

        if(!mMessage.getText().toString().isEmpty()){
            DatabaseReference newMessageDb = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID).push();
            Map newMessageMap = new HashMap();
            newMessageMap.put("text", mMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

            newMessageDb.updateChildren(newMessageMap);
        }

        mMessage.setText(null);
    }

    private void initializeRecyclerView() {

        messageList = new ArrayList<>();

        mChat = findViewById(R.id.messageList);

        mChat.setNestedScrollingEnabled(false); /* to make a seamless scrolling **/

        mChat.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mChat.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new MessageAdapter(messageList);

        mChat.setAdapter(mChatAdapter);
    }
}