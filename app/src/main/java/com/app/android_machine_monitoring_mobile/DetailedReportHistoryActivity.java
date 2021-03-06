package com.app.android_machine_monitoring_mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.report.DetailedReportHistoryAdapter;
import com.app.android_machine_monitoring_mobile.shared.report.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailedReportHistoryActivity extends AppCompatActivity implements DetailedReportHistoryAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private DetailedReportHistoryAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Report> mReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_report_history);

        mProgressCircle = findViewById(R.id.progress_circle);

        mRecyclerView = findViewById(R.id.rvImages);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReports = new ArrayList<>();
        mAdapter = new DetailedReportHistoryAdapter(this, mReports);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        String selectedKey = getIntent().getStringExtra("selectedKey");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reports").child(selectedKey);
        readReportsFromFirebase();


    }

    private void readReportsFromFirebase() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReports.clear();

                Report report = dataSnapshot.getValue(Report.class);
//                    report.setKey(dataSnapshot.getKey());
                mReports.add(report);

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailedReportHistoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
//        Report selectedItem = mReports.get(position);

    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show();
    }
}
