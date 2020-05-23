package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.machine.GetMachineData;
import com.app.android_machine_monitoring_mobile.shared.machine.Machine;
import com.app.android_machine_monitoring_mobile.shared.machine.RecyclerView_Config_BreakdownList;

import java.util.List;

public class BreakdownListActivity extends BaseActivity {

    private RecyclerView rvBreakdownList;
    private TextView txtNoBreakdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_list);

        rvBreakdownList = findViewById(R.id.rvBreakdownList);
        txtNoBreakdown = findViewById(R.id.txtNoBreakdown);
        setRvBreakdownList();

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



}
