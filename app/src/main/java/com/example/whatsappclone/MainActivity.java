package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneNumber, mVerificationCode;
    private Button mVerifyCodeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhoneNumber = findViewById(R.id.phoneNumber);
        mVerificationCode = findViewById(R.id.verificationCode);
        //mVerifyCodeBtn = findViewById(R.id.sendVerifyCodeBtn);
    }
}