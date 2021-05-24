package com.example.whatsappclone.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.UserModel;
import com.example.whatsappclone.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserModel> userList;

    public UserListAdapter(ArrayList<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @NotNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        /** it will inflate the view to recyclerView  **/
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp); /** to attach the above params **/

        UserListViewHolder rcv = new UserListViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserListAdapter.UserListViewHolder holder, int position) {

        holder.mUserName.setText(userList.get(position).getName());

        holder.mPhoneNumber.setText(userList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView mUserName, mPhoneNumber;

        public UserListViewHolder(View view) {
            super(view);

            mUserName = view.findViewById(R.id.userName);

            mPhoneNumber = view.findViewById(R.id.userPhoneNumber);
        }
    }
}
