package com.example.whatsappclone.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.MediaAdapter;
import com.example.whatsappclone.Adapters.MessageAdapter;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat, mMedia;
    private RecyclerView.Adapter mChatAdapter, mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager, mMediaLayoutManager;

    ArrayList<MessageModel> messageList;
    DatabaseReference mChatDb;

    String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");
        mChatDb = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        Button mSend = findViewById(R.id.send);
        Button mAddMedia = findViewById(R.id.addMedia);

        mSend.setOnClickListener(v -> sendMessage());

        mAddMedia.setOnClickListener(v -> openGallery());

        initializeMessage();
        initializeMedia();
        getMessages();
    }


    private void getMessages() {
        mChatDb.addChildEventListener(new ChildEventListener() {
            // onChildAdded is used because is checks all the child under the parent as soon as a new child is added
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String text = "",
                            creatorID = "";

                    if (snapshot.child("text").getValue() != null)
                        text = snapshot.child("text").getValue().toString();

                    if (snapshot.child("creator").getValue() != null)
                        creatorID = snapshot.child("creator").getValue().toString();

                    MessageModel mMessage = new MessageModel(snapshot.getKey(), creatorID, text);
                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size() - 1); // this will scroll to the latest message
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    int totalMediaUploaded = 0; // iterator for traversing the mediaIdlist
    ArrayList<String> mediaIdList = new ArrayList<>();
    EditText mMessage;

    private void sendMessage() {
        mMessage = findViewById(R.id.message);

        String messageId = mChatDb.push().getKey();
        DatabaseReference newMessageDb = mChatDb.child(messageId);

        final Map newMessageMap = new HashMap();
        newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

        if (!mMessage.getText().toString().isEmpty())
            newMessageMap.put("text", mMessage.getText().toString());

        //newMessageDb.updateChildren(newMessageMap);

        if (!mediaUriList.isEmpty()) {
            for (String mediaUri : mediaUriList) {
                String mediaId = newMessageDb.child("media").getKey();
                mediaIdList.add(mediaId);
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().
                        child("chat").child(chatID).child(messageId).child(mediaId);

                UploadTask uploadTask = filePath.putFile(Uri.parse(mediaUri));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newMessageMap.put("/media/" + mediaIdList.get(totalMediaUploaded) + "/", uri.toString());
                                totalMediaUploaded++;

                                if (totalMediaUploaded == mediaUriList.size())
                                    updateDatabaseWithNewMessage(newMessageDb, newMessageMap);

                            }
                        });
                    }
                });

            }
        } else {
            if (!mMessage.getText().toString().isEmpty())
                updateDatabaseWithNewMessage(newMessageDb, newMessageMap);
        }


    }

    private void updateDatabaseWithNewMessage(DatabaseReference newMessageDb, Map newMessageMap) {
        newMessageDb.updateChildren(newMessageMap);
        mMessage.setText(null);
        mediaUriList.clear();
        mediaIdList.clear();
        mMediaAdapter.notifyDataSetChanged();
    }

    int PICK_IMAGE_INTENT = 1;
    ArrayList<String> mediaUriList = new ArrayList<>();

    private void initializeMessage() {

        messageList = new ArrayList<>();

        mChat = findViewById(R.id.messageList);

        mChat.setNestedScrollingEnabled(false); /* to make a seamless scrolling **/

        mChat.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mChat.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new MessageAdapter(messageList);

        mChat.setAdapter(mChatAdapter);
    }

    private void initializeMedia() {

        mediaUriList = new ArrayList<>();

        mMedia = findViewById(R.id.mediaList);

        mMedia.setNestedScrollingEnabled(false); /* to make a seamless scrolling **/

        mMedia.setHasFixedSize(false);

        mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);

        mMedia.setLayoutManager(mMediaLayoutManager);

        mMediaAdapter = new MediaAdapter(getApplicationContext(), mediaUriList);

        mMedia.setAdapter(mMediaAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*"); // to pick images, to pick audio - audio/*
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // this will enable user to select multiple images
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /* if user selects more than 1 image then we cant store the array of all info in data intent,
            hence we check if there is more than one image selected, if not then we store all the image data
            in the array list
             */
            if (data.getClipData() == null)
                mediaUriList.add(data.getDataString());
            else {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                }
            }

            mMediaAdapter.notifyDataSetChanged();

        }
    }
}