package com.abhijith.nanodegree.geonotes.Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.abhijith.nanodegree.geonotes.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GooglePlayServicesHelper {

    private static final String TAG = "GooglePlayServiceHelper";

    public static boolean isGooglePlayServicesAvailable(Context context) {
        Log.d(TAG, "isGooglePlayServicesAvailable: checking google services version");

        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        if (ConnectionResult.SUCCESS == status)
            return true;
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(status)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isGooglePlayServicesAvailable: an error occurred but we can fix it");
            Toast.makeText(context, R.string.installGooglePlay_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
