package com.abhijith.nanodegree.geonotes.Utils;

import android.content.Context;

import com.abhijith.nanodegree.geonotes.Model.Notes;
import com.abhijith.nanodegree.geonotes.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MarkerClusterRenderer extends DefaultClusterRenderer<Notes> {

    private Context context;
    private String email;

    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<Notes> clusterManager, String email) {
        super(context, map, clusterManager);
        this.context = context;
        this.email = email;
        clusterManager.setRenderer(this);
    }

    @Override
    protected void onBeforeClusterItemRendered(Notes item, MarkerOptions markerOptions) {
        if (!item.getEmail().equals(email)) {
            markerOptions.icon(GeoNotesUtils.bitmapDescriptorFromVector(context, R.drawable.ic_marker_cluster_all)); //Here you retrieve BitmapDescriptor from ClusterItem and set it as marker icon
        } else {
            markerOptions.icon(GeoNotesUtils.bitmapDescriptorFromVector(context, R.drawable.ic_marker_cluster));
        }
        markerOptions.visible(true);
    }
}
