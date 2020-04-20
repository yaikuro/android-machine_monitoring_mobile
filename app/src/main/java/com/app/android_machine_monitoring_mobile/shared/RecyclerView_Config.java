package com.app.android_machine_monitoring_mobile.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.R;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private MachineAdapter machineAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Machine> machineList, List<String> keys) {
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
        private TextView machineStatus;

        private String key;

        public MachineItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext)
                    .inflate(R.layout.machine_list_item, parent, false));

            machineName = itemView.findViewById(R.id.txtMachineName);
            machineLine = itemView.findViewById(R.id.txtMachineLine);
            machineStation = itemView.findViewById(R.id.txtMachineStation);
            machineID = itemView.findViewById(R.id.txtMachineID);
            machineStatus = itemView.findViewById(R.id.txtMachineStatus);
        }

        public void bind(Machine machine, String key) {
            machineName.setText(machine.getMachineName());
            machineLine.setText(machine.getMachineLine());
            machineStation.setText(machine.getMachineStation());
            machineID.setText(machine.getMachineID());
            machineStatus.setText(machine.getMachineStatus());
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
