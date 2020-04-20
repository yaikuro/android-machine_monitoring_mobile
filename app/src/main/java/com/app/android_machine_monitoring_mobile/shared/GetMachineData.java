package com.app.android_machine_monitoring_mobile.shared;

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
    private DatabaseReference myRef;
    private List<Machine> machineList = new ArrayList<>();

    public GetMachineData() {
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Machines");
    }

    public void readMachinesData(final DataStatus dataStatus) {
        myRef.addValueEventListener(new ValueEventListener() {
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
