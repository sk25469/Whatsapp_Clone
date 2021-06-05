package com.example.whatsappclone.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.ChatModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    ArrayList<ChatModel> ChatList;

    public ChatListAdapter(ArrayList<ChatModel> ChatList) {
        this.ChatList = ChatList;
    }

    @NonNull
    @NotNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        /** it will inflate the view to recyclerView  **/
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp); /** to attach the above params **/

        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatListAdapter.ChatListViewHolder holder, int position) {

        holder.mTitle.setText(ChatList.get(position).getChatId());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLayout;

        public ChatListViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);

            mLayout = view.findViewById(R.id.layout);
        }
    }
}
