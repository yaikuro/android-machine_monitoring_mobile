package com.app.android_machine_monitoring_mobile.shared.machine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.R;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private MachineAdapter machineAdapter;

    private int[] colorStatusList = new int[]
            {
                    R.drawable.color_green,     // Running
                    R.drawable.color_red,       // Breakdown
                    R.drawable.color_yellow,    // Repairing
                    R.drawable.color_blue       // Waiting for confirmation
            };

    public void setMachineListConfig(RecyclerView recyclerView, Context context, List<Machine> machineList, List<String> keys) {
        mContext = context;
        int numberOfColumns = 2;

        machineAdapter = new MachineAdapter(machineList, keys);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(machineAdapter);
    }

    public void setBreakdownListConfig(RecyclerView recyclerView, Context context, List<Machine> machineList, List<String> keys) {
        mContext = context;

        machineAdapter = new MachineAdapter(machineList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(machineAdapter);
    }

    class MachineItemView extends RecyclerView.ViewHolder {
        private TextView machineName;
        private TextView machineLine;
        private TextView machineStation;
        private TextView machineID;
        private ImageView machineStatusColor;

        private String key;

        public MachineItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext)
                    .inflate(R.layout.machine_dashboard_list_item, parent, false));

            machineName = itemView.findViewById(R.id.txtMachineName);
            machineLine = itemView.findViewById(R.id.txtMachineLine);
            machineStation = itemView.findViewById(R.id.txtMachineStation);
            machineID = itemView.findViewById(R.id.txtMachineID);
            machineStatusColor = itemView.findViewById(R.id.ivStatusColor);
        }

        public void bind(Machine machine, String key) {
            machineName.setText(machine.getMachineName());
            machineLine.setText(machine.getMachineLine());
            machineStation.setText(machine.getMachineStation());
            machineID.setText(machine.getMachineID());
            machineStatusColor.setImageResource(colorStatusList[Integer.parseInt(machine.getMachineStatus()) - 1]);
            this.key = key;
        }
    }

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
