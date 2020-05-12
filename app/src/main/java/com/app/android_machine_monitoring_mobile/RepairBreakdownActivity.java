package com.app.android_machine_monitoring_mobile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.report.Report;
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

public class RepairBreakdownActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RepairBreakdownActivity";
    private static final int PICK_PROBLEM_IMAGE = 100;
    private static final int PICK_SOLUTION_IMAGE = 200;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseReportRef;
    private StorageReference mStorageRef;
    private StorageTask mProblemUploadTask;
    private StorageTask mSolutionUploadTask;
    private User user;

    private Uri mProblemImageUri;
    private Uri mSolutionImageUri;
    private String uid;
    private String fullName;
    private String machineLine;
    private String machineStation;
    private String machineID;
    private String currentResponseTime;
    private TextView txtMachineInfo;
    private TextView pic;
    private TextView txtCurrentTimeResponse;
    private EditText etProblemDescription;
    private EditText etSolutionDescription;
    private ImageView ivProblemPicture;
    private ImageView ivSolutionPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_breakdown);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUserRef = mDatabase.getReference("Users");
        mDatabaseReportRef = mDatabase.getReference("Report");
        mStorageRef = FirebaseStorage.getInstance().getReference("Report");
        readUserFromDatabase();

        machineLine = getIntent().getStringExtra("machineLine");
        machineStation = getIntent().getStringExtra("machineStation");
        machineID = getIntent().getStringExtra("machineID");
        currentResponseTime = getIntent().getStringExtra("currentResponseTime");

        // Buttons
        findViewById(R.id.btnSave).setOnClickListener(this);
        ivProblemPicture = findViewById(R.id.ivProblemPicture);
        ivSolutionPicture = findViewById(R.id.ivSolutionPicture);
        ivProblemPicture.setOnClickListener(this);
        ivSolutionPicture.setOnClickListener(this);
        findViewById(R.id.ivSolutionPicture).setOnClickListener(this);

        // Views
        txtMachineInfo = findViewById(R.id.txtMachineInfo);
        txtCurrentTimeResponse = findViewById(R.id.txtCurrentDate);
        pic = findViewById(R.id.txtPIC);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        etSolutionDescription = findViewById(R.id.etSolutionDescription);


        txtMachineInfo.setText("Line " + machineLine + ", " + "Station " + machineStation + ", " + "ID " + machineID);
        txtCurrentTimeResponse.setText(currentResponseTime);


    }

    private void readUserFromDatabase() {
        // Read from the database
        mDatabaseUserRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                user = dataSnapshot.getValue(User.class);
                pic.setText(user.getFullName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void takeProblemPicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_PROBLEM_IMAGE);
    }

    private void takeSolutionPicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_SOLUTION_IMAGE);
    }

    // Get the file extension and then change it to .jpg
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadReport() {
        if (mProblemImageUri != null && mSolutionImageUri != null) {

            final StorageReference problemFileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mProblemImageUri));

            final StorageReference solutionFileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mSolutionImageUri));

            mProblemUploadTask = problemFileReference.putFile(mProblemImageUri);
            mSolutionUploadTask = solutionFileReference.putFile(mSolutionImageUri);

            mProblemUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return problemFileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Report report = new Report(machineLine, machineStation, machineID, downloadUri.toString(), etProblemDescription.getText().toString(), currentResponseTime);
                        String uploadID = mDatabaseReportRef.push().getKey();
                        mDatabaseReportRef.child(uploadID).setValue(report);
                    } else {
                        // Handle failures
                        Toast.makeText(RepairBreakdownActivity.this, "Error: failed to upload to database", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mSolutionUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return solutionFileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Report report = new Report(machineLine, machineStation, machineID, downloadUri.toString(), etSolutionDescription.getText().toString(), currentResponseTime);
                        String uploadID = mDatabaseReportRef.push().getKey();
                        mDatabaseReportRef.child(uploadID).setValue(report);
                    } else {
                        // Handle failures
                        Toast.makeText(RepairBreakdownActivity.this, "Error: failed to upload to database", Toast.LENGTH_SHORT).show();
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
        if (id == R.id.btnSave) {
            if (mProblemUploadTask != null && mProblemUploadTask.isInProgress() && mSolutionUploadTask != null && mSolutionUploadTask.isInProgress()) {
                Toast.makeText(this, "Uploading report is in progress...", Toast.LENGTH_SHORT).show();
            } else {
                uploadReport();
            }
//            goto_MainDashboard();
        } else if (id == R.id.ivProblemPicture) {
            takeProblemPicture();
        } else if (id == R.id.ivSolutionPicture) {
            takeSolutionPicture();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PROBLEM_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mProblemImageUri = data.getData();

            Picasso.get()
                    .load(mProblemImageUri)
                    .into(ivProblemPicture);
        }
        if (requestCode == PICK_SOLUTION_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mSolutionImageUri = data.getData();

            Picasso.get()
                    .load(mSolutionImageUri)
                    .into(ivSolutionPicture);
        }
    }
}
