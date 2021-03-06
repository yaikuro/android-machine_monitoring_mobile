package com.app.android_machine_monitoring_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.report.Report;
import com.app.android_machine_monitoring_mobile.shared.report.ReportHistoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportHistoryActivity extends BaseActivity implements ReportHistoryAdapter.OnItemClickListener {
    private ReportHistoryAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Report> mReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        mProgressCircle = findViewById(R.id.progress_circle);

        RecyclerView mRecyclerView = findViewById(R.id.rvImages);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReports = new ArrayList<>();
        mAdapter = new ReportHistoryAdapter(this, mReports);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Reports");
        readReportsFromFirebase();


    }

    private void readReportsFromFirebase() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReports.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    assert report != null;
                    report.setKey(postSnapshot.getKey());
                    mReports.add(report);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReportHistoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Report selectedItem = mReports.get(position);
        String selectedKey = selectedItem.getKey();
        Intent i = new Intent(this, DetailedReportHistoryActivity.class);
        i.putExtra("selectedKey", selectedKey);
        startActivity(i);
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
