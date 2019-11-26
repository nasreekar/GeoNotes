package com.abhijith.nanodegree.geonotes.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import static com.abhijith.nanodegree.geonotes.Utils.Constants.MARKER_DIMENSION;

public class GeoNotesUtils {

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, MARKER_DIMENSION, MARKER_DIMENSION);
        Bitmap bitmap = Bitmap.createBitmap(MARKER_DIMENSION, MARKER_DIMENSION, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void showMarkerInfo(AppCompatActivity context, String title, String description) {
        new BottomSheetMaterialDialog.Builder(context)
                .setTitle(title)
                .setMessage(description)
                .setCancelable(false)
                .setPositiveButton("Okay", android.R.drawable.ic_menu_view, (dialogInterface, which) -> dialogInterface.dismiss())
                .build()
                .show();
    }
}
