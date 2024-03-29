package com.benschoenfeld.hrt.ontime;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

public class BusOverlay extends ItemizedOverlay
{
    private ArrayList<OverlayItem> _overlays = new ArrayList<OverlayItem>();
    private Context _context;

    public BusOverlay(Drawable defaultMarker, Context context)
    {
        super(boundCenterBottom(defaultMarker));
        _context = context;
    }

    public void addOverlay(OverlayItem overlay)
    {
        _overlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i)
    {
        return _overlays.get(i);
    }

    @Override
    public int size()
    {
        return _overlays.size();
    }

    @Override
    protected boolean onTap(int index)
    {
        OverlayItem item = _overlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(_context);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }
}
