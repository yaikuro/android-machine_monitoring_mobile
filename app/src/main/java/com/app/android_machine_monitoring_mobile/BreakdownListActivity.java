package com.app.android_machine_monitoring_mobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.machine.GetMachineData;
import com.app.android_machine_monitoring_mobile.shared.machine.Machine;
import com.app.android_machine_monitoring_mobile.shared.machine.RecyclerView_Config_BreakdownList;

import java.util.List;

public class BreakdownListActivity extends BaseActivity {

    private static final int CAMERA_PERM_CODE = 1;
    private RecyclerView rvBreakdownList;
    private TextView txtNoBreakdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_list);

        rvBreakdownList = findViewById(R.id.rvBreakdownList);
        txtNoBreakdown = findViewById(R.id.txtNoBreakdown);
        askCameraPermissions();

    }

    private void setRvBreakdownList() {
        new GetMachineData().readMachinesData(new GetMachineData.DataStatus() {
            @Override
            public void DataIsLoaded(List<Machine> machineList, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerView_Config_BreakdownList().setBreakdownListConfig(rvBreakdownList, BreakdownListActivity.this, machineList, keys);

                if (machineList.size() < 1) {
                    txtNoBreakdown.setText(R.string.no_breakdown);
                } else {
                    txtNoBreakdown.setText("");
                }
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            setRvBreakdownList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setRvBreakdownList();
            } else {
                txtNoBreakdown.setText(R.string.please_enable_camera_permission);
                Toast.makeText(this, "Camera permission is required to enable this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
