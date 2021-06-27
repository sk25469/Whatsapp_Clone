package com.example.whatsappclone.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.ChatListAdapter;
import com.example.whatsappclone.Models.ChatModel;
import com.example.whatsappclone.NotificationServices.SendNotification;
import com.example.whatsappclone.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class MainPageActivity extends AppCompatActivity implements OSSubscriptionObserver {

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;


    private static final String ONESIGNAL_APP_ID = "fed48090-0393-48db-bcdd-e40ad965f7bc";

    ArrayList<ChatModel> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.addSubscriptionObserver(this);


        new SendNotification("Yoooooo! Whats up?", "Message aaya hai be!", null);

        Fresco.initialize(this);

        Button logoutBtn = findViewById(R.id.logoutBtn);
        Button findUserBtn = findViewById(R.id.findUsersBtn);


        findUserBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FindUsersActivity.class)));


        logoutBtn.setOnClickListener(v -> {

            OneSignal.disablePush(true); // disables the one signal to push notification after we log out
            FirebaseAuth.getInstance().signOut();
            /** when user is signed out, he goes to the {@ MainActivity} */
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); /** so that the user doesn't have access to
             anything of this current activity **/
            startActivity(intent);

            finish();
        });

        initializeRecyclerView();
        getPermissions();
        getUserChatList();
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().isSubscribed() &&
                stateChanges.getTo().isSubscribed()) {
            FirebaseDatabase.getInstance().getReference().child("user").
                    child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .child("notificationKey").setValue(Objects.requireNonNull(OneSignal.getDeviceState()).getUserId());

            stateChanges.getTo().getUserId();
        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }


    private void getUserChatList() {
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().
                child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("chat");

        mUserChatDB.addValueEventListener(new ValueEventListener() { // value event listener checks for only the changes in the database
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        ChatModel mChat = new ChatModel(childSnapshot.getKey());
                        boolean exists = false;
                        for (ChatModel mChatIterator : chatList) {
                            if (mChatIterator.getChatId().equals(mChat.getChatId()))
                                exists = true;
                        }
                        if (exists)
                            continue;
                        chatList.add(mChat);
                        mChatListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void initializeRecyclerView() {

        chatList = new ArrayList<>();

        mChatList = findViewById(R.id.chatList);

        mChatList.setNestedScrollingEnabled(false); /* to make a seamless scrolling **/

        mChatList.setHasFixedSize(false);

        mChatListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mChatList.setLayoutManager(mChatListLayoutManager);

        mChatListAdapter = new ChatListAdapter(chatList);

        mChatList.setAdapter(mChatListAdapter);
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { /** this permission is asked only if the android version > Marshmallow **/
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }
}