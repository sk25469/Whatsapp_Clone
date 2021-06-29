package com.example.whatsappclone.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.UserModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserModel> userList;

    public UserListAdapter(ArrayList<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        /* it will inflate the view to recyclerView  **/
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp); /* to attach the above params **/

        UserListViewHolder rcv = new UserListViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull final UserListAdapter.UserListViewHolder holder, final int position) {

        holder.mUserName.setText(userList.get(position).getName());

        holder.mPhoneNumber.setText(userList.get(position).getPhone());
        holder.mLayout.setOnClickListener(v -> {
            createChat(holder.getAdapterPosition());
        });
    }

    private void createChat(int position) {
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
        assert key != null;
        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);
        newChatMap.put("users/" + userList.get(position).getUid(), true);

        DatabaseReference chatInfoDb = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        chatInfoDb.updateChildren(newChatMap);


        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().
                child("user");

        userDb.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).
                child("chat").
                child(key).setValue(true); // this is for the person who is sending the message

        userDb.child(userList.get(position).getUid()).
                child("chat").
                child(key).setValue(true); // for the person whom we are sending the message from our contacts list
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView mUserName, mPhoneNumber;
        public LinearLayout mLayout;

        public UserListViewHolder(View view) {
            super(view);

            mUserName = view.findViewById(R.id.userName);

            mPhoneNumber = view.findViewById(R.id.userPhoneNumber);
            mLayout = view.findViewById(R.id.layout);
        }
    }
}
