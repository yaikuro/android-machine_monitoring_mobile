package com.app.android_machine_monitoring_mobile.shared.machine;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetMachineData {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private List<Machine> machineList = new ArrayList<>();

    public GetMachineData() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Machines");
    }

    public void readMachinesData(final DataStatus dataStatus) {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machineList.clear();
                List<String> machines = new ArrayList<>();

                for (DataSnapshot lineNode : dataSnapshot.getChildren()) {
                    for (DataSnapshot machineNode : lineNode.getChildren()) {
                        machines.add(machineNode.getKey());
                        Machine machine = machineNode.getValue(Machine.class);
                        if (machine.getMachineStatus().equals("2")) {

//                             Should be removed after testing
//                            String currentBreakdownTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
//                            machine.setMachineBreakdownTime(currentBreakdownTime);
                            machineList.add(machine);
                        }
                    }
                }
                dataStatus.DataIsLoaded(machineList, machines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void readMachinesDataLine1(final DataStatus dataStatus) {
        mDatabaseRef.child("Line 1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machineList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Machine machine = keyNode.getValue(Machine.class);
                    machineList.add(machine);
                }
                dataStatus.DataIsLoaded(machineList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readMachinesDataLine2(final DataStatus dataStatus) {
        mDatabaseRef.child("Line 2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machineList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Machine machine = keyNode.getValue(Machine.class);
                    machineList.add(machine);
                }
                dataStatus.DataIsLoaded(machineList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public interface DataStatus {
        void DataIsLoaded(List<Machine> machineList, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }
}
