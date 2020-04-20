package com.app.android_machine_monitoring_mobile.shared;

public class Machine {
    private String machineName;
    private String machineLine;
    private String machineStation;
    private String machineID;
    private String machineStatus;

    public Machine() {
        // Default constructor required for calls to DataSnapshot.getValue(Machine.class)
    }

    public Machine(String machineName, String machineLine, String machineStation, String machineID, String machineStatus) {
        this.machineName = machineName;
        this.machineLine = machineLine;
        this.machineStation = machineStation;
        this.machineID = machineID;
        this.machineStatus = machineStatus;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
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

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }
}
