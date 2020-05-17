package com.app.android_machine_monitoring_mobile.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.R;
import com.app.android_machine_monitoring_mobile.shared.report.Report;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {
    private Context mContext;
    private List<Report> mReports;

    public ImagesAdapter(Context context, List<Report> reports) {
        mContext = context;
        mReports = reports;
    }


    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.images_listitem, parent, false);
        return new ImagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Report reportCurrent = mReports.get(position);
        holder.txtName.setText(reportCurrent.getReportImageDescription());
        Picasso.get()
                .load(reportCurrent.getReportImageUrl())
                .fit()
                .centerCrop()
                .into(holder.ivImageDescription);
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public ImageView ivImageDescription;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            ivImageDescription = itemView.findViewById(R.id.ivImage1);
        }
    }
}
