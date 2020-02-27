package com.consistentservices.baridhara.adapterDataBinder;

public class pumpData
{
    private String deviceName = "";
    private String device_id = "";
    private String status = "";
    private String phaseR = "";
    private String phaseY = "";
    private String phaseB = "";

    public pumpData(String deviceName, String deviceId, String status, String PhaseR, String PhaseY, String PhaseB)
    {
        this.deviceName = deviceName;
        this.device_id = deviceId;
        this.status = status;
        this.phaseR = PhaseR;
        this.phaseY = PhaseY;
        this.phaseB = PhaseB;
    }

    public String getName()
    {
        return  deviceName;
    }

    public String getDeviceID()
    {
        return device_id;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String Status)
    {
        this.status = Status;
    }

    public String getPhaseR()
    {
        return phaseR;
    }

    public void setPhaseR(String phaseR)
    {
        this.phaseR = phaseR;
    }

    public String getPhaseY()
    {
        return phaseY;
    }

    public void setPhaseY(String phaseY)
    {
        this.phaseY = phaseY;
    }


    public String getPhaseB()
    {
        return phaseB;
    }

    public void setPhaseB(String phaseB)
    {
        this.phaseB = phaseB;
    }
}
