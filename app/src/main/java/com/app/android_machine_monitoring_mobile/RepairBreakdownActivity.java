package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RepairBreakdownActivity extends AppCompatActivity {

    private String machineLine;
    private String machineStation;
    private String currentResponseTime;
    private TextView txtMachineLine;
    private TextView txtMachineStation;
    private TextView txtCurrentTimeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_breakdown);

        machineLine = getIntent().getStringExtra("machineLine");
        machineStation = getIntent().getStringExtra("machineStation");
        currentResponseTime = getIntent().getStringExtra("currentResponseTime");

        // Views
        txtMachineLine = findViewById(R.id.txtMachineLine);
        txtMachineStation = findViewById(R.id.txtMachineStation);
        txtCurrentTimeResponse = findViewById(R.id.txtCurrentTimeResponse);

        txtMachineLine.setText(machineLine);
        txtMachineStation.setText(machineStation);
        txtCurrentTimeResponse.setText(currentResponseTime);


    }
}
