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

public class RepairBreakdownActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RepairBreakdownActivity";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private User user;

    private String uid;
    private String fullName;
    private String machineLine;
    private String machineStation;
    private String machineID;
    private String currentResponseTime;
    private TextView txtMachineInfo;
    private TextView pic;
    private TextView txtCurrentTimeResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_breakdown);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Users");
        readUserFromDatabase();

        machineLine = getIntent().getStringExtra("machineLine");
        machineStation = getIntent().getStringExtra("machineStation");
        machineID = getIntent().getStringExtra("machineID");
        currentResponseTime = getIntent().getStringExtra("currentResponseTime");

        // Buttons
        findViewById(R.id.btnSave).setOnClickListener(this);

        // Views
        txtMachineInfo = findViewById(R.id.txtMachineInfo);
        txtCurrentTimeResponse = findViewById(R.id.txtCurrentDate);
        pic = findViewById(R.id.txtPIC);

        txtMachineInfo.setText("Line " + machineLine + ", " + "Station " + machineStation + ", " + "ID " + machineID);
        txtCurrentTimeResponse.setText(currentResponseTime);


    }

    private void readUserFromDatabase() {
        // Read from the database
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSave) {
            goto_MainDashboard();
        }
    }
}
