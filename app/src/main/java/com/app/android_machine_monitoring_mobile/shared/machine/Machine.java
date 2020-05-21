package com.app.android_machine_monitoring_mobile.shared.machine;

public class Machine {
    private String machineLine;
    private String machineStation;
    private String machineID;
    private String machineName;
    private String machineStatus;
    private String machineBreakdownTime;

    public Machine() {
        // Default constructor required for calls to DataSnapshot.getValue(Machine.class)
    }

    public Machine(String machineLine, String machineStation, String machineID, String machineName, String machineStatus, String machineBreakdownTime) {

        if (machineBreakdownTime.trim().equals("")) {
            machineBreakdownTime = "None";
        }

        this.machineLine = machineLine;
        this.machineStation = machineStation;
        this.machineID = machineID;
        this.machineName = machineName;
        this.machineStatus = machineStatus;
        this.machineBreakdownTime = machineBreakdownTime;
    }

    public String getMachineLine() {
        return machineLine;
    }

    public void setMachineLine(String machineLine) {
        this.machineLine = machineLine;
    }

    public String getMachineStation() {
        return machineStation;
    }

    public void setMachineStation(String machineStation) {
        this.machineStation = machineStation;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getMachineBreakdownTime() {
        return machineBreakdownTime;
    }

    public void setMachineBreakdownTime(String machineBreakdownTime) {
        this.machineBreakdownTime = machineBreakdownTime;
    }
}
