package com.benschoenfeld.hrt.ontime;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class BusRouteFactory
{
    private static ArrayList<BusCheckIn> fetchBusCheckIns()
    {
        try
        {
            Gson gson = new Gson();
            URL checkInsUrl = new URL("http://dl.dropbox.com/u/126246/hrt_bus_data.json");
            InputStream inputStream = checkInsUrl.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            BusCheckIn[] checkIns = gson.fromJson(result.toString(), BusCheckIn[].class);
            return new ArrayList<BusCheckIn>(Arrays.asList(checkIns));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static ArrayList<BusRoute> fetchBusRoutes()
    {
        ArrayList<BusCheckIn> checkIns = fetchBusCheckIns();
        ArrayList<BusRoute> routes = new ArrayList<BusRoute>();
        for(BusCheckIn checkIn : checkIns)
        {
            boolean routeAdded = false;
            int routeIndex = 0;
            for(BusRoute route : routes)
            {
                if(route.RouteNum == checkIn.Route)
                {
                    route.CheckIns.add(checkIn);
                    routeAdded = true;
                    break;
                }
                
                if(route.RouteNum > checkIn.Route)
                    break;

                routeIndex++;
            }

            if(!routeAdded)
            {
                BusRoute newRoute = new BusRoute();
                newRoute.RouteNum = checkIn.Route;
                newRoute.CheckIns.add(checkIn);
                routes.add(routeIndex, newRoute);
            }
        }

        return routes;
    }
}
