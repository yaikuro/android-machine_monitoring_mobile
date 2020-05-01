package com.app.android_machine_monitoring_mobile.shared.machine;

public class Machine {
    private String machineLine;
    private String machineStation;
    private String machineID;
    private String machineName;
    private String machineStatus;

    public Machine() {
        // Default constructor required for calls to DataSnapshot.getValue(Machine.class)
    }

    public Machine(String machineName, String machineLine, String machineStation, String machineID, String machineStatus) {
        this.machineLine = machineLine;
        this.machineStation = machineStation;
        this.machineID = machineID;
        this.machineName = machineName;
        this.machineStatus = machineStatus;
    }

    public String getMachineLine() {
        return machineLine;
    }

    public String getMachineStation() {
        return machineStation;
    }

    public String getMachineID() {
        return machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getMachineStatus() {
        return machineStatus;
    }


}
