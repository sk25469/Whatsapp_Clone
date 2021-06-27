package com.example.whatsappclone.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Activities.ChatActivity;
import com.example.whatsappclone.Models.ChatModel;
import com.example.whatsappclone.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    ArrayList<ChatModel> chatList;

    public ChatListAdapter(ArrayList<ChatModel> chatList) {
        this.chatList = chatList;
    }

    @NotNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /** it will inflate the view to recyclerView  **/
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp); // to attach the above params

        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ChatListViewHolder holder, int position) {

        holder.mTitle.setText(chatList.get(position).getChatId());

        holder.mLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("chatID", chatList.get(holder.getAdapterPosition()).getChatId());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public LinearLayout mLayout;

        public ChatListViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);

            mLayout = view.findViewById(R.id.layout);
        }
    }
}
