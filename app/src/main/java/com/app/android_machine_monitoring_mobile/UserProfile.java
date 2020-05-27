package com.app.android_machine_monitoring_mobile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.user.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfile extends BaseActivity implements View.OnClickListener {

    private static final int PICK_USER_PROFILE_PICTURE = 100;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private StorageTask<UploadTask.TaskSnapshot> mUserProfilePictureUploadTask;

    private String TAG = "UserProfile";
    private String uid, email, fullName, nickname, mobilePhoneNumber;
    private TextView txtHiUser;
    private TextView txtUserProfileName;
    private TextView txtUserProfileEmail;
    private TextView txtUserProfileMobilePhoneNumber;
    private TextView txtUserProfileUid;
    private ImageView ivUserProfilePicture;
    private Button btnSaveChanges;


    private User user;
    private Uri mUserProfilePictureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("Users");
        readUserInfoFromDatabase();

        // Views
        txtHiUser = findViewById(R.id.txtHiUser);
        txtUserProfileName = findViewById(R.id.txtUserProfileName);
        txtUserProfileEmail = findViewById(R.id.txtUserProfileEmail);
        txtUserProfileMobilePhoneNumber = findViewById(R.id.txtUserProfileMobilePhoneNumber);
        txtUserProfileUid = findViewById(R.id.txtUserProfileUserUid);

        // Buttons
        ivUserProfilePicture = findViewById(R.id.ivUserProfilePicture);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        ivUserProfilePicture.setOnClickListener(this);
        btnSaveChanges.setOnClickListener(this);
        findViewById(R.id.btnSignOut).setOnClickListener(this);

    }

    private void readUserInfoFromDatabase() {
        // Read user info from the database
        mDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                user = dataSnapshot.getValue(User.class);

                assert user != null;
                txtHiUser.setText(getString(R.string.stringHi, user.getNickname()));
                txtUserProfileName.setText(user.getFullName());
                txtUserProfileEmail.setText(user.getEmail());
                txtUserProfileMobilePhoneNumber.setText(user.getMobilePhoneNumber());
                txtUserProfileUid.setText(uid);

                if (user.getUserProfilePictureUrl() == null) {
                    ivUserProfilePicture.setImageResource(R.drawable.ic_person_black_24dp);
                } else {
                    Picasso.get()
                            .load(user.getUserProfilePictureUrl())
                            .fit()
                            .centerCrop()
                            .into(ivUserProfilePicture);

                    Log.d(TAG, "onDataChange: " + user.getUserProfilePictureUrl());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void takeUserProfilePicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_USER_PROFILE_PICTURE);
    }

    // Get the file extension and then change it to .jpg
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadUserProfilePicture() {
        if (mUserProfilePictureUri != null) {

            showProgressBar();

            final StorageReference userProfilePictureFileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mUserProfilePictureUri));

            mUserProfilePictureUploadTask = userProfilePictureFileReference.putFile(mUserProfilePictureUri);

            mUserProfilePictureUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return userProfilePictureFileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        mDatabaseRef.child(uid).child("userProfilePictureUrl").setValue(downloadUri.toString());

                        btnSaveChanges.setVisibility(View.INVISIBLE);

                        hideProgressBar();
                        Toast.makeText(UserProfile.this, "Profile picture updated", Toast.LENGTH_SHORT).show();

                    } else {
                        // Handle failures
                        Toast.makeText(UserProfile.this, "Failed to upload picture to database", Toast.LENGTH_SHORT).show();
                    }
                }

            });


        } else {
            Toast.makeText(this, "Error. No image taken", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSignOut) {
            mAuth.signOut();
            goto_LoginActivity();
        } else if (id == R.id.ivUserProfilePicture) {
            takeUserProfilePicture();
        } else if (id == R.id.btnSaveChanges) {
            uploadUserProfilePicture();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_USER_PROFILE_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUserProfilePictureUri = data.getData();

            Picasso.get()
                    .load(mUserProfilePictureUri)
                    .into(ivUserProfilePicture);

            btnSaveChanges.setVisibility(View.VISIBLE);
        }
    }
}
