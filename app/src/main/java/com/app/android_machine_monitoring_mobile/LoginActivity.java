package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "LoginActivty";

    private EditText etID;
    private EditText etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setProgressBar(R.id.progressBar);


        // Views
        etID = findViewById(R.id.etID);
        etPassword = findViewById(R.id.etPassword);

        // Buttons
        findViewById(R.id.btnSignInEmail).setOnClickListener(this);
        findViewById(R.id.txtRegister).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();


    } // End of onCreate

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            goto_MainDashboard();
        }
    }

    private void signInEmail(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            mAuth.getCurrentUser();
                            goto_MainDashboard();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        hideProgressBar();
                    }
                });
        // [END sign_in_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = etID.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) && (TextUtils.isEmpty(password))) {
            Toast.makeText(this, "Enter ID and Password", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            if (TextUtils.isEmpty(email)) {
                etID.setError("Required.");
                valid = false;
            } else {
                etID.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                valid = false;
            } else {
                etPassword.setError(null);
            }
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSignInEmail) {
            signInEmail(etID.getText().toString(), etPassword.getText().toString());
        } else if (i == R.id.txtRegister) {
            goto_RegisterActivity();
        }
    }
} // End of LoginActivity
