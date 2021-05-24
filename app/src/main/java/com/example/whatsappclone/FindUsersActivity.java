package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.whatsappclone.Adapters.UserListAdapter;
import com.example.whatsappclone.Models.UserModel;

import java.util.ArrayList;

public class FindUsersActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserModel> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        userList = new ArrayList<>();

        initializeRecyclerView();
        getContactList();
    }

    private void getContactList(){
        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);
        /** this cursor will go through all the contacts **/

        while(phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserModel mContact = new UserModel(name, phone);

            userList.add(mContact);

            /** as something changed we need to tell the adapter to display the changed data **/
            mUserListAdapter.notifyDataSetChanged();

        }
    }

    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);

        mUserList.setNestedScrollingEnabled(false); /** to make a seamless scrolling **/
        mUserList.setHasFixedSize(false);

        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mUserList.setLayoutManager(mUserListLayoutManager);

        mUserListAdapter = new UserListAdapter(userList);

        mUserList.setAdapter(mUserListAdapter);
    }
}