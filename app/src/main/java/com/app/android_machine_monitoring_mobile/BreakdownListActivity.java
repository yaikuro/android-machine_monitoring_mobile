package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.machine.GetMachineData;
import com.app.android_machine_monitoring_mobile.shared.machine.Machine;
import com.app.android_machine_monitoring_mobile.shared.machine.RecyclerView_Config;

import java.util.List;

public class BreakdownListActivity extends BaseActivity {

    private RecyclerView rvBreakdownList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_list);

        rvBreakdownList = findViewById(R.id.rvBreakdownList);
        setRvBreakdownList();

    }

    private void setRvBreakdownList() {
        new GetMachineData().readMachinesData(new GetMachineData.DataStatus() {
            @Override
            public void DataIsLoaded(List<Machine> machineList, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerView_Config().setBreakdownListConfig(rvBreakdownList, BreakdownListActivity.this, machineList, keys);
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
