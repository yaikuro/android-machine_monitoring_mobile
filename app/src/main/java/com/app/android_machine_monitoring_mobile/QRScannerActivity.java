package com.app.android_machine_monitoring_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler, View.OnClickListener {
    private ZXingScannerView mScannerView;

    private String machineLine;
    private String machineStation;
    private String machineID;
    private String machineName;
    private String qrCodeVerification;
    private String qrCodeResult;

    private TextView txtMachineLineInformation;
    private TextView txtMachineStationInformation;
    private TextView txtMachineIDInformation;

    // Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseMachineRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseMachineRef = mDatabase.getReference("Machines");

        // Get machine's information
        machineLine = getIntent().getStringExtra("machineLine");
        machineStation = getIntent().getStringExtra("machineStation");
        machineID = getIntent().getStringExtra("machineID");

        // Initialize QR code
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        // Buttons
        findViewById(R.id.btnSkipQRScan).setOnClickListener(this);

        // Views
        txtMachineLineInformation = findViewById(R.id.txtMachineLineInformation);
        txtMachineStationInformation = findViewById(R.id.txtMachineStationInformation);
        txtMachineIDInformation = findViewById(R.id.txtMachineIDInformation);

        txtMachineLineInformation.setText(machineLine);
        txtMachineStationInformation.setText(machineStation);
        txtMachineIDInformation.setText(machineID);


        qrCodeVerification = "Line " + machineLine + ", " + "Station " + machineStation + ", " + "ID " + machineID;

    } // End of onCreate

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        if (rawResult.getText().length() < 17) {
            mScannerView.resumeCameraPreview(this);
            Toast.makeText(this, "Wrong QR Code", Toast.LENGTH_SHORT).show();
        } else {
            qrCodeResult = rawResult.getText();
            if (qrCodeResult.equals(qrCodeVerification)) {
                goto_RepairBreakdownActivity();
            } else {
                mScannerView.resumeCameraPreview(this);
                Toast.makeText(this, "Machine ID does not match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    private void goto_RepairBreakdownActivity() {
        String currentResponseTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Set selected breakdown machine status to "Repairing"
        mDatabaseMachineRef.child("Line " + machineLine).child(machineID).child("machineStatus").setValue("3");

        Intent i = new Intent(this, RepairBreakdownActivity.class);
        i.putExtra("machineLine", machineLine);
        i.putExtra("machineStation", machineStation);
        i.putExtra("machineID", machineID);
//                i.putExtra("machineName", machineName);
        i.putExtra("currentResponseTime", currentResponseTime);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSkipQRScan) {
            goto_RepairBreakdownActivity();
        }
    }
}
