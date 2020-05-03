package com.app.android_machine_monitoring_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private String machineLine;
    private String machineStation;
    private String machineName;
    private String qrCodeVerification;
    private String qrCodeResult;

    private TextView txtMachineLineInformation;
    private TextView txtMachineStationInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        // Get machine's information
        machineLine = getIntent().getStringExtra("machineLine");
        machineStation = getIntent().getStringExtra("machineStation");
//        machineName = getIntent().getStringExtra("machineName");

        // Initialize QR code
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        // Views
        txtMachineLineInformation = findViewById(R.id.txtMachineLineInformation);
        txtMachineStationInformation = findViewById(R.id.txtMachineStationInformation);
        txtMachineLineInformation.setText("Line      : " + machineLine);
        txtMachineStationInformation.setText("Station : " + machineStation);


        qrCodeVerification = "Line " + machineLine + ", " + "Station " + machineStation;

    } // End of onCreate

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        if (rawResult.getText().length() < 17) {
            mScannerView.resumeCameraPreview(this);
            Toast.makeText(this, "Wrong QR Code", Toast.LENGTH_SHORT).show();
        } else {
            qrCodeResult = rawResult.getText().substring(0, 17);
            if (qrCodeResult.equals(qrCodeVerification)) {
                String currentResponseTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
                Intent i = new Intent(this, RepairBreakdownActivity.class);
                i.putExtra("machineLine", machineLine);
                i.putExtra("machineStation", machineStation);
//                i.putExtra("machineName", machineName);
                i.putExtra("currentResponseTime", currentResponseTime);
                startActivity(i);
                finish();
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

}
