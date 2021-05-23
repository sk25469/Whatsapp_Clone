package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText mPhoneNumber, mVerificationCode;
    private Button mVerifyCodeBtn;
    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        userIsLoggedIn();

        mPhoneNumber = findViewById(R.id.phoneNumber);
        mVerificationCode = findViewById(R.id.verificationCode);

        mVerifyCodeBtn = findViewById(R.id.sendVerifyCodeBtn);

        mVerifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVerificationId != null) {
                    verifyPhoneNumberWithCode();
                } else
                    startPhoneNumberVerification();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @org.jetbrains.annotations.NotNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                signInWithPhoneCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull @org.jetbrains.annotations.NotNull FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

            }

            @Override
            public void onCodeSent(@NonNull @NotNull String code, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(code, forceResendingToken);

                Log.d(TAG, "onCodeSent:" + code);

                mVerificationId = code;
                mVerifyCodeBtn.setText("Verify Code");
            }
        };
    }

    private void verifyPhoneNumberWithCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mVerificationCode.getText().toString());
        signInWithPhoneCredentials(credential);
    }

    private void signInWithPhoneCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    userIsLoggedIn();
                }
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
            return;
        }
    }

    // Function for phone number verification
    private void startPhoneNumberVerification() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(mPhoneNumber.getText().toString())       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)              // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}