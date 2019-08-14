package com.abhijith.nanodegree.geonotes.Model;

import android.content.Context;
import android.widget.Toast;

import com.abhijith.nanodegree.geonotes.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GooglePlayServicesHelper {

    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(context, R.string.installGooglePlay_error, Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
