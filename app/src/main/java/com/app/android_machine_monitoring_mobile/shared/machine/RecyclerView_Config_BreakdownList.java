package com.app.android_machine_monitoring_mobile.shared.machine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.R;
import com.app.android_machine_monitoring_mobile.UserProfile;

import java.util.List;

public class RecyclerView_Config_BreakdownList {
    private Context mContext;
    private MachineAdapter machineAdapter;

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
        private TextView machineName;
        private String key;
        private ImageView machineStatusColor;


        public MachineItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext)
                    .inflate(R.layout.breakdown_list_item, parent, false));

            machineLine = itemView.findViewById(R.id.txtMachineLine);
            machineStation = itemView.findViewById(R.id.txtMachineStation);
//            machineID = itemView.findViewById(R.id.txtMachineID);
//            machineName = itemView.findViewById(R.id.txtMachineName);
            machineStatusColor = itemView.findViewById(R.id.ivStatusColor_BreakdownList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserProfile.class);
                    Toast.makeText(mContext, machineLine.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, machineStation.getText().toString(), Toast.LENGTH_SHORT).show();
                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(Machine machine, String key) {
            machineLine.setText(machine.getMachineLine());
            machineStation.setText(machine.getMachineStation());
//            machineID.setText(machine.getMachineID());
//            machineName.setText(machine.getMachineName());
            machineStatusColor.setImageResource(colorStatusList[Integer.parseInt(machine.getMachineStatus()) - 1]);
            this.key = key;
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
            holder.bind(mMachineList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mMachineList.size();
        }
    }
}
