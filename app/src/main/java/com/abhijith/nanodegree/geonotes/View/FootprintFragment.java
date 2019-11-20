package com.abhijith.nanodegree.geonotes.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.abhijith.nanodegree.geonotes.R;
import com.abhijith.nanodegree.geonotes.Utils.GooglePlayServicesHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FootprintFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = FootprintFragment.class.getSimpleName();

    private static final float DEFAULT_ZOOM = 15f;

    @BindView(R.id.tv_search)
    TextView mSearchText;

    @BindView(R.id.ic_gps)
    ImageView mGps;

    private EditText title;
    private EditText description;
    private TextView date;
    private TextView formLocation;
    String finalLocation;

    private GoogleMap mMap;
    private Location currentLocation;

    private boolean mLocationPermissionsGranted = false;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATIONS_PERMISSIONS_REQUEST = 5445;

    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    private AutocompleteSupportFragment autocompleteFragment;


    public FootprintFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLocationPermissionsGranted) {
            if (GooglePlayServicesHelper.isGooglePlayServicesAvailable(this.getContext())) {
                getLocationPermissions();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_footprint, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map fragment");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg_footprint);

        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.API_KEY));
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        if (autocompleteFragment != null) {
            autocompleteFragment.setHint("Search Location");
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        }

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map is ready");
        this.mMap = googleMap;

        if (mLocationPermissionsGranted) {
            Log.d(TAG, "onMapReady: Getting current location of the device and moving the camera");

            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false); // to get rid of the location button

            init();
        }

        googleMap.setOnInfoWindowClickListener(marker -> {
            Log.d(TAG, "setOnInfoWindowClickListener: Marker window clicked");
            Toast.makeText(this.getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
        });

    }

    private void init() {
        Log.d(TAG, "init: initializing");
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                mSearchText.setText(place.getName());
                geoLocate();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }

        });

        mGps.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked gps icon");
            getDeviceLocation();
        });

        mMap.setOnMapClickListener(this::displayNoteDialog);

        mMap.setOnMarkerClickListener(marker -> {
            displayMarkerWithNotes(marker);
            return true;
        });
    }


    private void displayNoteDialog(LatLng latLng) {
        dialog = new AlertDialog.Builder(this.getContext());
        inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.geonotes_logo_round);
        dialog.setTitle("Add Notes");

        title = dialogView.findViewById(R.id.et_title);
        description = dialogView.findViewById(R.id.et_description);
        date = dialogView.findViewById(R.id.tv_date);
        formLocation = dialogView.findViewById(R.id.tv_location);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(Calendar.getInstance().getTime());
        date.setText(currentDate);

        formLocation.setText(getLocationDetails(latLng));

        clearFields();

        dialog.setPositiveButton("SUBMIT", (dialog, which) -> {

            // add notes in db

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title.toString());
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            dialog.dismiss();
        });

        dialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        dialog.show();
    }

    private void clearFields() {
        title.setText(null);
        description.setText(null);
    }

    private void displayMarkerWithNotes(Marker marker) {
        Toast.makeText(this.getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();
        Log.d(TAG, "geoLocate: searchString: " + searchString);
        Geocoder geocoder = new Geocoder(this.getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
            Toast.makeText(getActivity(), getString(R.string.error_retrieving_location_coordinates), Toast.LENGTH_SHORT).show();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), address.getAddressLine(0));
        }
    }

    // Getting the base location of the user
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting device's current location");
        FusedLocationProviderClient mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        try {
            if (mLocationPermissionsGranted) {
                Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                "My Location");

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getActivity(), getString(R.string.current_location_not_found), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException");
        }
    }

    private String getLocationDetails(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this.getContext());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            finalLocation = getString(R.string.service_not_available);
            Log.e(TAG, finalLocation, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            finalLocation = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, finalLocation + ". " +
                    "Latitude = " + latLng.latitude +
                    ", Longitude = " +
                    latLng.longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (finalLocation.isEmpty()) {
                finalLocation = getString(R.string.no_address_found);
                Log.e(TAG, finalLocation);
            }
        } else {
            Address address = addresses.get(0);
            Log.i(TAG, getString(R.string.address_found));
            finalLocation = address.getAddressLine(0);
        }
        return finalLocation;
    }

    private void getLocationPermissions() {
        Log.d(TAG, "getLocationPermissions: getting location permissions");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATIONS_PERMISSIONS_REQUEST);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATIONS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: called");
        mLocationPermissionsGranted = false;
        if (requestCode == LOCATIONS_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: permission failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                mLocationPermissionsGranted = true;
                // initialize our map
                initMap();
            }
        }
    }

    private void moveCamera(LatLng latLng, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));

    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(DEFAULT_ZOOM).build();
    }
}
