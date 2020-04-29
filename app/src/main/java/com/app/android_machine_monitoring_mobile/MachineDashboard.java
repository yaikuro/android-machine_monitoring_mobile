package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.machine.GetMachineData;
import com.app.android_machine_monitoring_mobile.shared.machine.Machine;
import com.app.android_machine_monitoring_mobile.shared.machine.RecyclerView_Config;

import java.util.List;

public class MachineDashboard extends BaseActivity {

    private RecyclerView rvMachineListLine1, rvMachineListLine2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_dashboard);

        rvMachineListLine1 = findViewById(R.id.rvMachineListLine1);
        rvMachineListLine2 = findViewById(R.id.rvMachineListLine2);
        setRvMachineListLine1();
        setRvMachineListLine2();

    }

    private void setRvMachineListLine1() {
        new GetMachineData().readMachinesDataLine1(new GetMachineData.DataStatus() {
            @Override
            public void DataIsLoaded(List<Machine> machineList, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(rvMachineListLine1, MachineDashboard.this, machineList, keys);
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

    private void setRvMachineListLine2() {
        new GetMachineData().readMachinesDataLine2(new GetMachineData.DataStatus() {
            @Override
            public void DataIsLoaded(List<Machine> machineList, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(rvMachineListLine2, MachineDashboard.this, machineList, keys);
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

    private void setRvBreakdownList() {
        new GetMachineData().readMachinesDataLine2(new GetMachineData.DataStatus() {
            @Override
            public void DataIsLoaded(List<Machine> machineList, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(rvMachineListLine2, MachineDashboard.this, machineList, keys);
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
