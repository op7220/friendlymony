package com.nect.friendlymony.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;

public class GetUserLocationActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    GetUserLocationActivity getUserLocationActivity;
    SupportMapFragment mapFragment;
    View mapView;

    GoogleMap googleMap;

    double mLatitude = 0, mLongitude = 0;
    MarkerOptions markerCurrentLocation;
    LatLng currentLocation;
    boolean isAskPermission = false;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    Address address;
    boolean isMapSync = false;
    TextView tvStoreAddress;
    Button btShare;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_location);

        getUserLocationActivity = GetUserLocationActivity.this;
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.agl_googleMap);
        mapView = mapFragment.getView();

        tvStoreAddress = findViewById(R.id.agl_tvStoreAddress);
        btShare = findViewById(R.id.agl_btShare);
        /*View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);*/

        initGoogleApi();
        mapFragment.getMapAsync(this);
        btShare.setOnClickListener(this);
    }


    private void initGoogleApi() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

        if (mLatitude == 0 && mLongitude == 0) {
            checkLocationPermission();
        }

        Log.e("GetLocationActivity", "init== ");
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getUserLocationActivity, mPermission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{mPermission}, Constants.LOCATION_PERMISSION_REQUEST_ID);
            isAskPermission = true;

        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        if (AppUtils.isGPSOn(getUserLocationActivity)) {
            if (mLatitude != 0 && mLongitude != 0) {
                //callLocationApi();
            } else {
                if (mLatitude == 0 && mLongitude == 0) {
                    showSettingsAlert();
                }
            }
        } else {
            if (mLatitude == 0 && mLongitude == 0) {
                showSettingsAlert();
            }
        }
    }


    private void showSettingsAlert() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("FindError", "All location settings are satisfied.");

                        //callLocationApi();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("FindError", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        //fetchLocation();
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getUserLocationActivity, Constants.REQUEST_CODE_PERMISSION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("FindError", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("FindError", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == btShare) {
            Intent intent = getIntent();
            if (!tvStoreAddress.getText().toString().equalsIgnoreCase("")) {
                if (address != null) {
                    intent.putExtra(Constants.CALLBACK_DELIVERY_ADDRESS, address.getAddressLine(0));
                    Log.e("GetLocationActivity", "CALLBACK_DELIVERY_ADDRESS== " + address.getAddressLine(0));
                } else {
                    intent.putExtra(Constants.CALLBACK_DELIVERY_ADDRESS, "");
                }
            } else {
                intent.putExtra(Constants.CALLBACK_DELIVERY_ADDRESS, "");
                Toast.makeText(getUserLocationActivity, getResources().getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
            }
            setResult(Constants.RESULT_CHOOSE_LOCATION, intent);
            finish();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getUserLocationActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getUserLocationActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            if (!isMapSync) {
                if (mLatitude != 0 && mLongitude != 0) {
                    mapFragment.getMapAsync(this);
                    Log.e("GetLocationActivity", "onConnected=MapAsync= ");
                }
            }

            //    ApplicationClass.toasts.showMessage(currentLatitude + " WORKS " + currentLongitude);
        }


        Log.e("GetLocationActivity", "onConnected==Latitude== " + mLatitude);
        Log.e("GetLocationActivity", "onConnected==Longitude== " + mLongitude);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            if (!isMapSync) {
                mapFragment.getMapAsync(getUserLocationActivity);
            }

            Log.e("GetLocationActivity", "onLocationChanged==currentLatitude== " + mLatitude);
            Log.e("GetLocationActivity", "onLocationChanged==currentLongitude== " + mLongitude);
        }
    }

    @Override
    public void onCameraIdle() {
        if (markerCurrentLocation != null) {
            currentLocation = googleMap.getCameraPosition().target;
            markerCurrentLocation.position(currentLocation);

            if (AppUtils.isNetworkAvailable(getUserLocationActivity)) {
                address = AppUtils.getAddressFromLatLong(getUserLocationActivity, currentLocation.latitude, currentLocation.longitude);
                if (address != null) {
                    tvStoreAddress.setText(address.getAddressLine(0));
                } else {
                    address = AppUtils.getAddressFromLatLong(getUserLocationActivity, currentLocation.latitude, currentLocation.longitude);
                    if (address != null) {
                        tvStoreAddress.setText(address.getAddressLine(0));
                    }
                    Log.e("GetLocationActivity", "onCameraIdle==address==null ");
                }
            } else {
                Toast.makeText(getUserLocationActivity, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }

            // Supported formats are: #RRGGBB #AARRGGBB
            //   #AA is the alpha, or amount of transparency

            Log.e("GetLocationActivity", "onCameraIdle==currentLatitude== " + currentLocation.latitude);
            Log.e("GetLocationActivity", "onCameraIdle==currentLongitude== " + currentLocation.longitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;


        if (mLatitude != 0 && mLongitude != 0) {
            setMapData();
        }
        //googleMap.setOnMarkerDragListener(this);
        googleMap.setOnCameraIdleListener(this);
    }


    private void setMapData() {
        currentLocation = new LatLng(mLatitude, mLongitude);


        markerCurrentLocation = new MarkerOptions()
                .position(currentLocation);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().isScrollGesturesEnabled();
        googleMap.setBuildingsEnabled(true);
//            googleMap.addMarker(markerCurrentLocation);


/*        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLatitude, mLongitude), 16));*/

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLocation).zoom(15f).tilt(45).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        isMapSync = true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            if (resultCode == RESULT_OK) {
                //fetchLocation();
                mapFragment.getMapAsync(this);
            } else {
                //retryPage();
                finish();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_ID) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                //fetchLocation();
                showSettingsAlert();
                mapFragment.getMapAsync(this);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getUserLocationActivity,
                        mPermission)) {
                    showSettingsAlert();
                }
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            //Now lets connect to the API
            mGoogleApiClient.connect();
        }
        permissionGranted();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    private void permissionGranted() {
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(3000); // 3 second, in milliseconds
    }

}
