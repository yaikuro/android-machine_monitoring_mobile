package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "RegisterActivity";

    private EditText etRegisterID;
    private EditText etRegisterPassword;
    private EditText etRegisterConfirmedPassword;
    private EditText etRegisterFullName;
    private EditText etRegisterNickname;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setProgressBar(R.id.progressBar);


        // Views
        etRegisterID = findViewById(R.id.etRegisterID);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirmedPassword = findViewById(R.id.etRegisterConfirmPassword);
        etRegisterFullName = findViewById(R.id.etRegisterFullName);
        etRegisterNickname = findViewById(R.id.etRegisterNickname);

        // Buttons
        findViewById(R.id.btnSignUpEmail).setOnClickListener(this);
        findViewById(R.id.txtSignIn).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

    } // End of onCreate

    private void signUpEmail(final String email, final String password, final String confirmedPassword, final String fullName, final String nickname) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            writeNewUser(email, fullName, nickname);
                            Log.d(TAG, "createUserWithEmail:success");
                            goto_MainDashboard();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                        hideProgressBar();
                    }
                });
        // [END create_user_with_email]
    } // End of signUpEmail()

    private void writeNewUser(String email, String fullName, String nickname) {
        String uid = mAuth.getUid();
//        FirebaseUser userProfile = mAuth.getCurrentUser();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(fullName)
//                .build();
//
//        userProfile.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User profile updated.");
//                        }
//                    }
//                });
        User user = new User(uid, email, fullName, nickname);
        mDatabase.child(user.getUid()).setValue(user);
    }

    private boolean validateForm() {
        boolean valid = true;

        String id = etRegisterID.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String confirmedPassword = etRegisterConfirmedPassword.getText().toString();
        String fullName = etRegisterFullName.getText().toString();
        String nickname = etRegisterNickname.getText().toString();

        if (TextUtils.isEmpty(id)) {
            etRegisterID.setError("Required.");
            valid = false;
        } else {
            etRegisterID.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError("Required.");
            valid = false;
        } else {
            etRegisterPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmedPassword)) {
            etRegisterConfirmedPassword.setError("Required.");
            valid = false;
        } else if (!password.equals(confirmedPassword)) {
            etRegisterConfirmedPassword.setError("Password confirmation does not match.");
            valid = false;
        } else {
            etRegisterConfirmedPassword.setError(null);
        }

        if (TextUtils.isEmpty(fullName)) {
            etRegisterFullName.setError("Required.");
            valid = false;
        } else {
            etRegisterFullName.setError(null);
        }

        if (TextUtils.isEmpty(nickname)) {
            etRegisterNickname.setError("Required.");
            valid = false;
        } else {
            etRegisterNickname.setError(null);
        }


        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSignUpEmail) {
            signUpEmail(etRegisterID.getText().toString(),
                    etRegisterPassword.getText().toString(),
                    etRegisterConfirmedPassword.getText().toString(),
                    etRegisterFullName.getText().toString(),
                    etRegisterNickname.getText().toString());
        } else if (i == R.id.txtSignIn) {
            goto_LoginActivity();
        }

    }
}
