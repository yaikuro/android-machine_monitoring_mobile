package com.app.android_machine_monitoring_mobile.shared.report;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.android_machine_monitoring_mobile.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailedReportHistoryAdapter extends RecyclerView.Adapter<DetailedReportHistoryAdapter.ImagesViewHolder> {
    private Context mContext;
    private List<Report> mReports;
    private OnItemClickListener mListener;

    public DetailedReportHistoryAdapter(Context context, List<Report> reports) {
        mContext = context;
        mReports = reports;
    }


    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.detailed_report_history_listitem, parent, false);
        return new ImagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Report reportCurrent = mReports.get(position);
        holder.txtReportPIC.setText(reportCurrent.getReportPIC());
        holder.txtReportLine.setText(reportCurrent.getReportMachineLine());
        holder.txtReportStation.setText(reportCurrent.getReportMachineStation());
        holder.txtReportID.setText(reportCurrent.getReportMachineID());
        holder.txtReportResponseTime.setText(reportCurrent.getReportResponseTime());
        holder.txtReportUploadTime.setText(reportCurrent.getReportUploadTime());
        holder.txtReportRepairDuration.setText(reportCurrent.getReportRepairDuration());
        holder.txtReportProblemDescription.setText(reportCurrent.getReportProblemImageDescription());
        holder.txtReportSolutionDescription.setText(reportCurrent.getReportSolutionImageDescription());

        Picasso.get()
                .load(reportCurrent.getReportProblemImageUrl())
                .placeholder(R.drawable.ic_image_black_24dp)
                .fit()
                .centerCrop()
                .into(holder.ivReportProblemImage);

        Picasso.get()
                .load(reportCurrent.getReportSolutionImageUrl())
                .placeholder(R.drawable.ic_image_black_24dp)
                .fit()
                .centerCrop()
                .into(holder.ivReportSolutionImage);
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView txtReportPIC;
        public TextView txtReportLine;
        public TextView txtReportStation;
        public TextView txtReportID;
        public TextView txtReportResponseTime;
        public TextView txtReportUploadTime;
        public TextView txtReportRepairDuration;
        public TextView txtReportProblemDescription;
        public TextView txtReportSolutionDescription;
        public ImageView ivReportProblemImage;
        public ImageView ivReportSolutionImage;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtReportPIC = itemView.findViewById(R.id.txtReportPIC);
            txtReportLine = itemView.findViewById(R.id.txtReportLine);
            txtReportStation = itemView.findViewById(R.id.txtReportStation);
            txtReportID = itemView.findViewById(R.id.txtReportID);
            txtReportResponseTime = itemView.findViewById(R.id.txtReportResponseTime);
            txtReportUploadTime = itemView.findViewById(R.id.txtReportUploadTime);
            txtReportRepairDuration = itemView.findViewById(R.id.txtReportRepairDuration);
            txtReportProblemDescription = itemView.findViewById(R.id.txtReportProblemDescription);
            txtReportSolutionDescription = itemView.findViewById(R.id.txtReportSolutionDescription);
            ivReportProblemImage = itemView.findViewById(R.id.ivReportProblemImage);
            ivReportSolutionImage = itemView.findViewById(R.id.ivReportSolutionImage);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do Whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
}
