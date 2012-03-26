package com.benschoenfeld.hrt.ontime;

import java.io.Serializable;

public class BusCheckIn implements Serializable
{
    public String CheckinTime;
    public int BusId;
    public String Lat;
    public String Lon;
    public int Adherence;
    public int Route;
    public int Direction;
    
    public String getTitle()
    {
        return "Bus #" + Integer.toString(BusId);
    }
    
    public String getDetails()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(CheckinTime);
        builder.append("\n");

        builder.append("Travelling ");
        builder.append(Direction == 1 ? "Outbound" : "Inbound");
        builder.append("\n");

        if(Adherence == 0)
            builder.append("On Time");
        else
        {
            builder.append(Math.abs(Adherence));
            builder.append(Math.abs(Adherence) > 1 ? " Minutes" : " Minute");
            builder.append(Adherence > 0 ? " Early" : " Late");
        }

        return builder.toString();
    }
}
