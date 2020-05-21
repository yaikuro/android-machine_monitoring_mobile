package com.app.android_machine_monitoring_mobile.shared.machine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.QRScannerActivity;
import com.app.android_machine_monitoring_mobile.R;

import java.util.List;

public class RecyclerView_Config_BreakdownList {
    private Context mContext;
    private MachineAdapter machineAdapter;
    private static final int ZBAR_CAMERA_PERMISSION = 100;

    private int[] colorStatusList = new int[]
            {
                    R.drawable.color_green,     // Running
                    R.drawable.color_red,       // Breakdown
                    R.drawable.color_yellow,    // Repairing
                    R.drawable.color_blue       // Waiting for confirmation
            };

    public void setBreakdownListConfig(RecyclerView recyclerView, Context context, List<Machine> machineList, List<String> keys) {
        mContext = context;

        machineAdapter = new MachineAdapter(machineList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(machineAdapter);
    }

    // ViewHolder
    class MachineItemView extends RecyclerView.ViewHolder {
        private TextView machineLine;
        private TextView machineStation;
        private TextView machineID;
        private TextView machineBreakdownTime;
        private ImageView machineStatusColor;


        public MachineItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext)
                    .inflate(R.layout.breakdown_listitem, parent, false));

            machineLine = itemView.findViewById(R.id.txtMachineLine);
            machineStation = itemView.findViewById(R.id.txtMachineStation);
            machineID = itemView.findViewById(R.id.txtMachineID);
            machineBreakdownTime = itemView.findViewById(R.id.txtMachineBreakdownTime);
            machineStatusColor = itemView.findViewById(R.id.ivStatusColor_BreakdownList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
                    } else {
                        Intent intent = new Intent(mContext, QRScannerActivity.class);
                        intent.putExtra("machineLine", machineLine.getText().toString());
                        intent.putExtra("machineStation", machineStation.getText().toString());
                        intent.putExtra("machineID", machineID.getText().toString());
                        intent.putExtra("machineBreakdownTime", machineBreakdownTime.getText().toString());
                        mContext.startActivity(intent);
                    }
                }
            });

        }

        public void bind(Machine machine) {
            machineLine.setText(machine.getMachineLine());
            machineStation.setText(machine.getMachineStation());
            machineID.setText(machine.getMachineID());
            machineBreakdownTime.setText(machine.getMachineBreakdownTime());
            machineStatusColor.setImageResource(colorStatusList[Integer.parseInt(machine.getMachineStatus()) - 1]);
        }

    }


    // Adapter
    class MachineAdapter extends RecyclerView.Adapter<MachineItemView> {
        private List<Machine> mMachineList;
        private List<String> mKeys;

        public MachineAdapter(List<Machine> machineList, List<String> mKeys) {
            this.mMachineList = machineList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public MachineItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MachineItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MachineItemView holder, int position) {
            holder.bind(mMachineList.get(position));
        }

        @Override
        public int getItemCount() {
            return mMachineList.size();
        }
    }

}
