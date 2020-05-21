package com.app.android_machine_monitoring_mobile.shared.report;

import com.google.firebase.database.Exclude;

public class Report {
    private String reportMachineLine;
    private String reportMachineStation;
    private String reportMachineID;
    private String reportProblemImageUrl;
    private String reportSolutionImageUrl;
    private String reportProblemImageDescription;
    private String reportSolutionImageDescription;
    private String reportUploadTime;

    private String mKey;

    public Report() {
        // Empty constructor needed
    }

    public Report(String reportMachineLine, String reportMachineStation, String reportMachineID, String reportImageUrl, String reportProblemImageDescription,
                  String reportSolutionImageUrl, String reportSolutionImageDescription, String reportUploadTime) {

        if (reportProblemImageDescription.trim().equals("")) {
            reportProblemImageDescription = "No description";
        }

        this.reportMachineLine = reportMachineLine;
        this.reportMachineStation = reportMachineStation;
        this.reportMachineID = reportMachineID;
        this.reportProblemImageUrl = reportImageUrl;
        this.reportProblemImageDescription = reportProblemImageDescription;
        this.reportSolutionImageUrl = reportSolutionImageUrl;
        this.reportSolutionImageDescription = reportSolutionImageDescription;
        this.reportUploadTime = reportUploadTime;
    }

    public String getReportMachineLine() {
        return reportMachineLine;
    }

    public void setReportMachineLine(String reportMachineLine) {
        this.reportMachineLine = reportMachineLine;
    }

    public String getReportMachineStation() {
        return reportMachineStation;
    }

    public void setReportMachineStation(String reportMachineStation) {
        this.reportMachineStation = reportMachineStation;
    }

    public String getReportMachineID() {
        return reportMachineID;
    }

    public void setReportMachineID(String reportMachineID) {
        this.reportMachineID = reportMachineID;
    }

    public String getReportProblemImageUrl() {
        return reportProblemImageUrl;
    }

    public void setReportProblemImageUrl(String reportProblemImageUrl) {
        this.reportProblemImageUrl = reportProblemImageUrl;
    }

    public String getReportSolutionImageUrl() {
        return reportSolutionImageUrl;
    }

    public void setReportSolutionImageUrl(String reportSolutionImageUrl) {
        this.reportSolutionImageUrl = reportSolutionImageUrl;
    }

    public String getReportProblemImageDescription() {
        return reportProblemImageDescription;
    }

    public void setReportProblemImageDescription(String reportProblemImageDescription) {
        this.reportProblemImageDescription = reportProblemImageDescription;
    }

    public String getReportSolutionImageDescription() {
        return reportSolutionImageDescription;
    }

    public void setReportSolutionImageDescription(String reportSolutionImageDescription) {
        this.reportSolutionImageDescription = reportSolutionImageDescription;
    }

    public String getReportUploadTime() {
        return reportUploadTime;
    }

    public void setReportUploadTime(String reportUploadTime) {
        this.reportUploadTime = reportUploadTime;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }
}
