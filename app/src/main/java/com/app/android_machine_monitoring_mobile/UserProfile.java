package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends BaseActivity implements View.OnClickListener {

    private String TAG = "UserProfile";
    private String uid, email, fullName, nickname, mobilePhoneNumber;
    private TextView txtHiUser,
            txtUserProfileName,
            txtUserProfileEmail,
            txtUserProfileMobilePhoneNumber,
            txtUserProfileUid;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Users");
        readUserInfoFromDatabase();

        // Views
        txtHiUser = findViewById(R.id.txtHiUser);
        txtUserProfileName = findViewById(R.id.txtUserProfileName);
        txtUserProfileEmail = findViewById(R.id.txtUserProfileEmail);
        txtUserProfileMobilePhoneNumber = findViewById(R.id.txtUserProfileMobilePhoneNumber);
        txtUserProfileUid = findViewById(R.id.txtUserProfileUserUid);

        // Buttons
        findViewById(R.id.btnSignOut).setOnClickListener(this);

    }

    private void readUserInfoFromDatabase() {
        // Read user info from the database
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                user = dataSnapshot.getValue(User.class);

                txtHiUser.setText("Hi, " + user.getNickname());
                txtUserProfileName.setText(user.getFullName());
                txtUserProfileEmail.setText(user.getEmail());
                txtUserProfileMobilePhoneNumber.setText(user.getMobilePhoneNumber());
                txtUserProfileUid.setText(uid);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSignOut) {
            mAuth.signOut();
            goto_LoginActivity();
        }
    }
}
