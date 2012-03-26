package com.benschoenfeld.hrt.ontime;

import java.io.Serializable;
import java.util.ArrayList;

public class BusRoute implements Serializable
{
    public int RouteNum;
    public ArrayList<BusCheckIn> CheckIns = new ArrayList<BusCheckIn>();

    @Override
    public String toString()
    {
        return Integer.toString(RouteNum);
    }
}
