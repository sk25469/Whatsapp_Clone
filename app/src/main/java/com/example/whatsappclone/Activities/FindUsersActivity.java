package com.example.whatsappclone.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Adapters.UserListAdapter;
import com.example.whatsappclone.Models.UserModel;
import com.example.whatsappclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class FindUsersActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserModel> userList, contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        contactList = new ArrayList<>();
        userList = new ArrayList<>();

        initializeRecyclerView();
        getContactList();
    }

    private void getContactList() {
        String ISOPrefix = getCountryISO();
        Cursor phones = getContentResolver().query(
                /* this cursor will go through all the contacts **/
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);


        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if(!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOPrefix + phone;

            UserModel mContact = new UserModel("", name, phone);

            contactList.add(mContact);

            /* as something changed we need to tell the adapter to display the changed data **/
            //mUserListAdapter.notifyDataSetChanged();
            
            getUserDetails(mContact); /* fetches the data from firebase database and returns the
            contact having data */

        }
    }

    private void getUserDetails(UserModel mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        /* We query the data from database to our contacts and check if it matches */
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = "", phone = "";

                    // this for loop goes through all the data entries in the database
                    for(DataSnapshot childSnapshot : snapshot.getChildren()){

                        if(childSnapshot.child("phone").getValue() != null)
                            phone = Objects.requireNonNull(childSnapshot.child("phone").getValue()).toString();

                        if(childSnapshot.child("name").getValue() != null)
                            name = Objects.requireNonNull(childSnapshot.child("name").getValue()).toString();

                        UserModel mUser = new UserModel(childSnapshot.getKey(), name,phone);
                        if(name.equals(phone)){
                            for(UserModel mContactIterator : contactList){
                                if(mContactIterator.getPhone().equals(mUser.getPhone()))
                                    mUser.setName(mContactIterator.getName());
                            }
                        }
                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();

                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private String getCountryISO() {
        String iso = "+91";
//        getApplicationContext();
//        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
//        if (telephonyManager.getNetworkCountryIso() != null)
//            if (telephonyManager.getNetworkCountryIso().equals(""))
//                iso = telephonyManager.getNetworkCountryIso();
//
//
//        //assert iso != null;
//        return CountryToPhonePrefix.getPhone(iso);
////        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
////        return tm.getSimCountryIso();
        return iso;
    }

    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);

        mUserList.setNestedScrollingEnabled(false); /* to make a seamless scrolling **/
        mUserList.setHasFixedSize(false);

        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mUserList.setLayoutManager(mUserListLayoutManager);

        mUserListAdapter = new UserListAdapter(userList);

        mUserList.setAdapter(mUserListAdapter);
    }
}