package com.benschoenfeld.hrt.ontime;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BusRouteListActivity extends Activity implements AdapterView.OnItemClickListener, TextWatcher
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_checkins);

        ListView routeList = (ListView)findViewById(R.id.route_list);
        routeList.setAdapter(new BusRouteArrayAdapter(BusRouteListActivity.this, new ArrayList<BusRoute>()));
        routeList.setOnItemClickListener(this);

        ((EditText)findViewById(R.id.route_filter)).addTextChangedListener(this);

        new DownloadFeedTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        BusRoute route = (BusRoute)adapterView.getItemAtPosition(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("route", route);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this, MapActivity.class);

        this.startActivity(intent);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        ListView routeList = (ListView)findViewById(R.id.route_list);
        BusRouteArrayAdapter busRouteArrayAdapter = (BusRouteArrayAdapter)routeList.getAdapter();
        busRouteArrayAdapter.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable s) {}

    private class DownloadFeedTask extends AsyncTask<Void, Void, Void>
    {
        private boolean errorOccurred = false;
        private List<BusRoute> routes;

        @Override
        protected void onPreExecute()
        {
            Toast.makeText(BusRouteListActivity.this,
                           "Downloading Data",
                           Toast.LENGTH_SHORT)
                    .show();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                routes = BusRouteFactory.fetchBusRoutes();
            }
            catch (Exception e)
            {
                errorOccurred = true;
                routes = new ArrayList<BusRoute>();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            ListView routeList = (ListView)findViewById(R.id.route_list);
            BusRouteArrayAdapter busRouteArrayAdapter = (BusRouteArrayAdapter)routeList.getAdapter();
            busRouteArrayAdapter.clear();
            for(BusRoute route : routes)
                busRouteArrayAdapter.add(route);

            setProgressBarIndeterminateVisibility(false);

            if(errorOccurred)
            {
                Toast.makeText(BusRouteListActivity.this,
                               "Unable to download data",
                               Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
