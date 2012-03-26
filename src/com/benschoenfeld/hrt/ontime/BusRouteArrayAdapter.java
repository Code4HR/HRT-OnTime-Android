package com.benschoenfeld.hrt.ontime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BusRouteArrayAdapter extends ArrayAdapter<BusRoute>
{
    private Context context;

    public BusRouteArrayAdapter(Context context, List<BusRoute> routes)
    {
        super(context, R.layout.bus_checkin_row, routes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.bus_checkin_row, parent, false);

        TextView route = (TextView) rowView.findViewById(R.id.route);
        route.setText("Route " + Integer.toString(getItem(position).RouteNum));

        TextView numBuses = (TextView) rowView.findViewById(R.id.buses_running);
        numBuses.setText(Integer.toString(getItem(position).CheckIns.size()) + " Buses Running");

        return rowView;
    }
}
