package com.abhijith.nanodegree.geonotes.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abhijith.nanodegree.geonotes.Model.Notes;
import com.abhijith.nanodegree.geonotes.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import static com.abhijith.nanodegree.geonotes.Utils.Constants.MARKER_DIMENSION;

public class MarkerClusterRenderer extends DefaultClusterRenderer<Notes> {
    private final IconGenerator iconGenerator;
    private final ImageView markerImageView;

    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<Notes> clusterManager) {
        super(context, map, clusterManager);
        iconGenerator = new IconGenerator(context);
        markerImageView = new ImageView(context);
        markerImageView.setLayoutParams(new ViewGroup.LayoutParams(MARKER_DIMENSION, MARKER_DIMENSION));
        iconGenerator.setContentView(markerImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(Notes item, MarkerOptions markerOptions) {
        markerImageView.setImageResource(R.drawable.ic_marker_cluster);
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        markerOptions.title(item.getTitle());
    }
}
