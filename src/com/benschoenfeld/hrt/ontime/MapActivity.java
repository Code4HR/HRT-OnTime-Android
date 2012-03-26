package com.benschoenfeld.hrt.ontime;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.maps.*;

import java.util.List;

public class MapActivity extends com.google.android.maps.MapActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            BusRoute route = (BusRoute)bundle.getSerializable("route");
            new LoadMapTask().execute(route);
        }
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }

    private class MapSpan
    {
        int minLat = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int minLon = Integer.MAX_VALUE;
        int maxLon = Integer.MIN_VALUE;
        double fitFactor = 1.5;

        public void add(GeoPoint point)
        {
            int latitudeE6 = point.getLatitudeE6();
            int longitudeE6 = point.getLongitudeE6();

            maxLat = Math.max(latitudeE6, maxLat);
            minLat = Math.min(latitudeE6, minLat);
            maxLon = Math.max(longitudeE6, maxLon);
            minLon = Math.min(longitudeE6, minLon);
        }
        
        public int getLat()
        {
            return (int) (Math.abs(maxLat - minLat) * fitFactor);
        }
        
        public int getLon()
        {
            return (int)(Math.abs(maxLon - minLon) * fitFactor);
        }

        public GeoPoint getMid()
        {
            return new GeoPoint( (maxLat + minLat)/2, (maxLon + minLon)/2 );
        }
    }

    private class LoadMapTask extends AsyncTask<BusRoute, Void, Void>
    {
        MapView mapView;
        MapSpan mapSpan;

        @Override
        protected void onPreExecute()
        {
            mapView = (MapView) findViewById(R.id.mapview);
            mapSpan = new MapSpan();
        }

        @Override
        protected Void doInBackground(BusRoute... routes)
        {
            List<Overlay> mapOverlays = mapView.getOverlays();

            LocationManager locationManager = (LocationManager) MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(lastKnownLocation != null)
            {
                GeoPoint geoPoint = new GeoPoint(
                        (int)(lastKnownLocation.getLatitude() * 1000000),
                        (int)(lastKnownLocation.getLongitude() * 1000000));
                mapSpan.add(geoPoint);

                Drawable drawable = MapActivity.this.getResources().getDrawable(R.drawable.map_pin_icon);
                BusOverlay userOverlay = new BusOverlay(drawable, MapActivity.this);

                OverlayItem overlayitem = new OverlayItem(geoPoint, "You are here", "");
                userOverlay.addOverlay(overlayitem);
                mapOverlays.add(userOverlay);
            }

            Drawable drawable = MapActivity.this.getResources().getDrawable(R.drawable.bus);
            BusOverlay busOverlays = new BusOverlay(drawable, MapActivity.this);

            for(BusCheckIn checkIn : routes[0].CheckIns)
            {
                double lat = Double.parseDouble(checkIn.Lat);
                double lon = Double.parseDouble(checkIn.Lon);

                GeoPoint geoPoint = new GeoPoint((int)(lat * 1000000), (int)(lon * 1000000));
                mapSpan.add(geoPoint);

                OverlayItem overlayitem = new OverlayItem(geoPoint, checkIn.getTitle(), checkIn.getDetails());
                busOverlays.addOverlay(overlayitem);
            }

            mapOverlays.add(busOverlays);

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            MapController mapController = mapView.getController();
            mapController.zoomToSpan(mapSpan.getLat(), mapSpan.getLon());
            mapController.animateTo(mapSpan.getMid());
        }
    }
}
