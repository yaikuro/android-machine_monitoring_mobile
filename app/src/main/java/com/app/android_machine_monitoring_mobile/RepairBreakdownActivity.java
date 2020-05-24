package com.app.android_machine_monitoring_mobile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Chronometer;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RepairBreakdownActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RepairBreakdownActivity";
    private static final int PICK_PROBLEM_IMAGE = 100;
    private static final int PICK_SOLUTION_IMAGE = 200;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseMachineRef;
    private DatabaseReference mDatabaseReportRef;
    private StorageReference mStorageRef;
    private StorageTask<UploadTask.TaskSnapshot> mProblemUploadTask;
    private StorageTask<UploadTask.TaskSnapshot> mSolutionUploadTask;
    private User user;

    private boolean running;
    private Chronometer chronometer;
    private Uri mProblemImageUri;
    private Uri mSolutionImageUri;
    private String uid;
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
        mDatabaseMachineRef = mDatabase.getReference("Machines");
        mDatabaseReportRef = mDatabase.getReference("Reports");
        mStorageRef = FirebaseStorage.getInstance().getReference("Reports");
        readUserFromDatabase();

        // Buttons
        findViewById(R.id.btnSave).setOnClickListener(this);
        ivProblemPicture = findViewById(R.id.ivProblemPicture);
        ivSolutionPicture = findViewById(R.id.ivSolutionPicture);
        ivProblemPicture.setOnClickListener(this);
        ivSolutionPicture.setOnClickListener(this);

        // Views
        txtMachineInfo = findViewById(R.id.txtMachineInfo);
        txtCurrentTimeResponse = findViewById(R.id.txtCurrentDate);
        pic = findViewById(R.id.txtPIC);
        etProblemDescription = findViewById(R.id.etProblemDescription);
        etSolutionDescription = findViewById(R.id.etSolutionDescription);

        // Get variables from previous activity
        machineLine = getIntent().getStringExtra("machineLine");
        machineStation = getIntent().getStringExtra("machineStation");
        machineID = getIntent().getStringExtra("machineID");
        currentResponseTime = getIntent().getStringExtra("currentResponseTime");

        // Start stopwatch
        chronometer = findViewById(R.id.chronometer);
        startChronometer();


        txtMachineInfo.setText(getString(R.string.stringMachineInfo, machineLine, machineStation, machineID));
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
                assert user != null;
                pic.setText(user.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        chronometer.stop();
        final String repairDuration = chronometer.getText().toString();
        final String uploadID = mDatabaseReportRef.push().getKey();
        final String currentUploadTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());


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
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return problemFileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        assert downloadUri != null;
                        Report report = new Report(user.getFullName(), machineLine, machineStation, machineID, downloadUri.toString(), etProblemDescription.getText().toString(),
                                "", "", currentResponseTime, currentUploadTime, repairDuration);
                        assert uploadID != null;
                        mDatabaseReportRef.child(uploadID).setValue(report);
                    } else {
                        // Handle failures
                        Toast.makeText(RepairBreakdownActivity.this, "Failed to upload to database", Toast.LENGTH_SHORT).show();
                    }

                    mSolutionUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }

                            // Continue with the task to get the download URL
                            return solutionFileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                String solutionDescription = etSolutionDescription.getText().toString();
                                if (solutionDescription.trim().equals("")) {
                                    solutionDescription = "No Description";
                                }

                                assert uploadID != null;
                                assert downloadUri != null;

                                mDatabaseReportRef.child(uploadID).child("reportSolutionImageUrl").setValue(downloadUri.toString());
                                mDatabaseReportRef.child(uploadID).child("reportSolutionImageDescription").setValue(solutionDescription);

                                // Set machine status to "Confirming"
                                mDatabaseMachineRef.child("Line " + machineLine).child(machineID).child("machineStatus").setValue("4");
                                goto_MainDashboard();
                            } else {
                                // Handle failures
                                Toast.makeText(RepairBreakdownActivity.this, "Failed to upload to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            });


        } else {
            Toast.makeText(this, "Error. No image taken", Toast.LENGTH_SHORT).show();
        }
    }

    public void startChronometer() {
        if (!running) {
            running = true;
            chronometer.setFormat("%s");
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSave) {
            if (ivProblemPicture.getDrawable() == null || ivSolutionPicture.getDrawable() == null) {
                Toast.makeText(this, "Please pick an image for both problem and solution", Toast.LENGTH_SHORT).show();
            } else {
                if (mProblemUploadTask != null && mProblemUploadTask.isInProgress() && mSolutionUploadTask != null && mSolutionUploadTask.isInProgress()) {
                    Toast.makeText(this, "Uploading report is in progress...", Toast.LENGTH_SHORT).show();
                } else {
                    uploadReport();
                }
            }

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
