package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.GetMachineData;
import com.app.android_machine_monitoring_mobile.shared.Machine;
import com.app.android_machine_monitoring_mobile.shared.RecyclerView_Config;

import java.util.List;

public class MachineDashboard extends BaseActivity {

    private RecyclerView rvMachineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);

        rvMachineList = findViewById(R.id.rvMachineList);
        new GetMachineData().readMachinesData(new GetMachineData.DataStatus() {
            @Override
            public void DataIsLoaded(List<Machine> machineList, List<String> keys) {
                new RecyclerView_Config().setConfig(rvMachineList, MachineDashboard.this, machineList, keys);
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
}
