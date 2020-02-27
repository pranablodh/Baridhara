package com.consistentservices.baridhara.adapterDataBinder;

public class pumpDataManage
{
    private  String deviceName = "";
    private  String id = "";

    public pumpDataManage(String deviceName, String id)
    {
        this.deviceName = deviceName;
        this.id = id;
    }

    public String getName()
    {
        return  deviceName;
    }

    public String getID()
    {
        return id;
    }

}
