package com.app.android_machine_monitoring_mobile.shared.report;

public class Report {
    private String reportMachineLine;
    private String reportMachineStation;
    private String reportMachineID;
    private String reportImageUrl;
    private String reportImageDescription;
    private String reportUploadTime;

    public Report() {
        // Empty constructor needed
    }

    public Report(String reportMachineLine, String reportMachineStation, String reportMachineID, String reportImageUrl, String reportImageDescription, String reportUploadTime) {

        if (reportImageDescription.trim().equals("")) {
            reportImageDescription = "No description";
        }

        this.reportMachineLine = reportMachineLine;
        this.reportMachineStation = reportMachineStation;
        this.reportMachineID = reportMachineID;
        this.reportImageUrl = reportImageUrl;
        this.reportImageDescription = reportImageDescription;
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

    public String getReportImageUrl() {
        return reportImageUrl;
    }

    public void setReportImageUrl(String reportImageUrl) {
        this.reportImageUrl = reportImageUrl;
    }

    public String getReportImageDescription() {
        return reportImageDescription;
    }

    public void setReportImageDescription(String reportImageDescription) {
        this.reportImageDescription = reportImageDescription;
    }

    public String getReportUploadTime() {
        return reportUploadTime;
    }

    public void setReportUploadTime(String reportUploadTime) {
        this.reportUploadTime = reportUploadTime;
    }
}
